package com.xime.averapizza.service;

import com.xime.averapizza.dto.*;
import com.xime.averapizza.model.*;
import com.xime.averapizza.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrediccionService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PrediccionPedidoRepository prediccionRepository;
    private final ProductoRepository productoRepository;
    private final SaborPizzaRepository saborRepository;

    /**
     * Genera predicciones para un rango de fechas
     */
    @Transactional
    public List<PrediccionDetalladaDTO> generarPredicciones(PrediccionRequestDTO request) {
        log.info("Generando predicciones desde {} hasta {}",
                request.getFechaInicio(), request.getFechaFin());

        List<PrediccionDetalladaDTO> resultado = new ArrayList<>();
        LocalDate fechaActual = request.getFechaInicio();

        while (!fechaActual.isAfter(request.getFechaFin())) {
            PrediccionDetalladaDTO prediccion = generarPrediccionParaFecha(
                    fechaActual,
                    request.getProductoId(),
                    request.getTipoPrediccion()
            );
            resultado.add(prediccion);
            fechaActual = fechaActual.plusDays(1);
        }

        return resultado;
    }

    /**
     * Genera predicción para una fecha específica
     */
    private PrediccionDetalladaDTO generarPrediccionParaFecha(
            LocalDate fecha, Long productoId, String tipoPrediccion) {

        DayOfWeek diaSemana = fecha.getDayOfWeek();

        // Obtener datos históricos de los últimos 30 días
        LocalDateTime inicioHistorico = fecha.atStartOfDay().minusDays(30);
        LocalDateTime finHistorico = fecha.atStartOfDay().minusDays(1).plusHours(23).plusMinutes(59);

        List<DetallePedido> historico = detallePedidoRepository
                .findByFechaHoraBetween(inicioHistorico, finHistorico);

        // Agrupar por producto, sabor y presentación
        Map<String, List<DetallePedido>> agrupados = agruparDetalles(historico);

        List<PrediccionItemDTO> items = new ArrayList<>();
        int totalPedidos = 0;
        double sumaConfianza = 0;

        for (Map.Entry<String, List<DetallePedido>> entry : agrupados.entrySet()) {
            List<DetallePedido> detallesGrupo = entry.getValue();

            if (productoId != null &&
                    !detallesGrupo.get(0).getProducto().getId().equals(productoId)) {
                continue;
            }

            PrediccionItemDTO item = calcularPrediccionItem(
                    detallesGrupo,
                    diaSemana,
                    tipoPrediccion
            );

            items.add(item);
            totalPedidos += item.getCantidad();
            sumaConfianza += item.getConfianza();

            // Guardar en BD
            guardarPrediccion(fecha, detallesGrupo.get(0), item);
        }

        double confianzaPromedio = items.isEmpty() ? 0 : sumaConfianza / items.size();

        return PrediccionDetalladaDTO.builder()
                .fecha(fecha)
                .items(items)
                .totalPedidos(totalPedidos)
                .confianzaPromedio(confianzaPromedio)
                .build();
    }

    /**
     * Calcula la predicción para un item específico
     */
    private PrediccionItemDTO calcularPrediccionItem(
            List<DetallePedido> historico,
            DayOfWeek diaSemana,
            String tipoPrediccion) {

        DetallePedido primero = historico.get(0);

        // Filtrar por día de la semana similar
        List<DetallePedido> mismosDias = historico.stream()
                .filter(d -> d.getPedido().getFechaHora().getDayOfWeek() == diaSemana)
                .collect(Collectors.toList());

        // Calcular cantidad predicha
        int cantidadPredicha;
        double confianza;

        if ("DIA_SEMANA".equals(tipoPrediccion) && !mismosDias.isEmpty()) {
            double promedio = mismosDias.stream()
                    .mapToInt(DetallePedido::getCantidad)
                    .average()
                    .orElse(0);
            cantidadPredicha = (int) Math.round(promedio);
            confianza = calcularConfianza(mismosDias.size(), 4); // 4 semanas ideales
        } else {
            // Promedio simple de últimos 7 días
            List<DetallePedido> ultimos7 = historico.stream()
                    .filter(d -> d.getPedido().getFechaHora()
                            .isAfter(LocalDateTime.now().minusDays(7)))
                    .collect(Collectors.toList());

            double promedio = ultimos7.stream()
                    .mapToInt(DetallePedido::getCantidad)
                    .average()
                    .orElse(0);
            cantidadPredicha = (int) Math.round(promedio);
            confianza = calcularConfianza(ultimos7.size(), 7);
        }

        // Calcular histórico
        HistoricoVentasDTO historicoDTO = calcularHistorico(historico);

        return PrediccionItemDTO.builder()
                .productoNombre(primero.getProducto().getNombre())
                .saborNombre(primero.getSabor1() != null ? primero.getSabor1().getNombre() : "N/A")
                .presentacion(primero.getPresentacion() != null ?
                        primero.getPresentacion().getTipo().name() : "ESTANDAR")
                .cantidad(cantidadPredicha)
                .confianza(confianza)
                .historico(historicoDTO)
                .build();
    }

    /**
     * Calcula el histórico de ventas
     */
    private HistoricoVentasDTO calcularHistorico(List<DetallePedido> historico) {
        LocalDateTime hace7dias = LocalDateTime.now().minusDays(7);
        LocalDateTime hace30dias = LocalDateTime.now().minusDays(30);
        LocalDateTime ayer = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0);

        double promedio7 = historico.stream()
                .filter(d -> d.getPedido().getFechaHora().isAfter(hace7dias))
                .mapToInt(DetallePedido::getCantidad)
                .average()
                .orElse(0);

        double promedio30 = historico.stream()
                .filter(d -> d.getPedido().getFechaHora().isAfter(hace30dias))
                .mapToInt(DetallePedido::getCantidad)
                .average()
                .orElse(0);

        int ventasAyer = historico.stream()
                .filter(d -> d.getPedido().getFechaHora().isAfter(ayer) &&
                        d.getPedido().getFechaHora().isBefore(ayer.plusDays(1)))
                .mapToInt(DetallePedido::getCantidad)
                .sum();

        String tendencia = promedio7 > promedio30 * 1.1 ? "CRECIENTE" :
                promedio7 < promedio30 * 0.9 ? "DECRECIENTE" : "ESTABLE";

        return HistoricoVentasDTO.builder()
                .promedioUltimos7Dias(Math.round(promedio7 * 100.0) / 100.0)
                .promedioUltimos30Dias(Math.round(promedio30 * 100.0) / 100.0)
                .ventasAyer(ventasAyer)
                .tendencia(tendencia)
                .build();
    }

    /**
     * Calcula confianza basada en cantidad de datos
     */
    private double calcularConfianza(int muestras, int ideal) {
        if (muestras >= ideal) return 95.0;
        if (muestras >= ideal * 0.75) return 85.0;
        if (muestras >= ideal * 0.5) return 70.0;
        if (muestras >= ideal * 0.25) return 55.0;
        return 40.0;
    }

    /**
     * Agrupa detalles por producto-sabor-presentación
     */
    private Map<String, List<DetallePedido>> agruparDetalles(List<DetallePedido> detalles) {
        return detalles.stream()
                .collect(Collectors.groupingBy(d -> {
                    String productoId = d.getProducto().getId().toString();
                    String saborId = d.getSabor1() != null ? d.getSabor1().getId().toString() : "null";
                    String presentacion = d.getPresentacion() != null ?
                            d.getPresentacion().getTipo().name() : "ESTANDAR";
                    return productoId + "-" + saborId + "-" + presentacion;
                }));
    }

    /**
     * Guarda la predicción en la BD
     */
    private void guardarPrediccion(LocalDate fecha, DetallePedido detalle, PrediccionItemDTO item) {
        PrediccionPedido prediccion = new PrediccionPedido();
        prediccion.setFechaPrediccion(fecha);
        prediccion.setProducto(detalle.getProducto());
        prediccion.setSabor(detalle.getSabor1());
        prediccion.setTipoPresentacion(detalle.getPresentacion() != null ?
                detalle.getPresentacion().getTipo() : null);
        prediccion.setCantidadPredicha(item.getCantidad());
        prediccion.setConfianza(item.getConfianza());
        prediccion.setFechaCalculo(LocalDateTime.now());
        prediccion.setTipoPrediccion(PrediccionPedido.TipoPrediccion.DIA_SEMANA);

        prediccionRepository.save(prediccion);
    }

    /**
     * Obtiene estadísticas de ventas
     */
    public EstadisticasVentasDTO obtenerEstadisticas(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        List<DetallePedido> detalles = detallePedidoRepository
                .findByFechaHoraBetween(inicioDateTime, finDateTime);

        int totalPedidos = (int) detalles.stream()
                .map(d -> d.getPedido().getId())
                .distinct()
                .count();

        double ventaPromedio = detalles.stream()
                .collect(Collectors.groupingBy(d -> d.getPedido().getFechaHora().toLocalDate()))
                .values()
                .stream()
                .mapToInt(List::size)
                .average()
                .orElse(0);

        // Producto más vendido
        Map.Entry<String, Long> productoTop = detalles.stream()
                .collect(Collectors.groupingBy(d -> d.getProducto().getNombre(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        // Sabor más vendido
        Map.Entry<String, Long> saborTop = detalles.stream()
                .filter(d -> d.getSabor1() != null)
                .collect(Collectors.groupingBy(d -> d.getSabor1().getNombre(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        // Ventas por día
        List<VentaPorDiaDTO> ventasPorDia = detalles.stream()
                .collect(Collectors.groupingBy(d -> d.getPedido().getFechaHora().toLocalDate()))
                .entrySet().stream()
                .map(e -> VentaPorDiaDTO.builder()
                        .fecha(e.getKey())
                        .diaSemana(e.getKey().getDayOfWeek()
                                .getDisplayName(TextStyle.FULL, new Locale("es", "ES")))
                        .cantidadPedidos((int) e.getValue().stream()
                                .map(d -> d.getPedido().getId())
                                .distinct()
                                .count())
                        .totalVentas(e.getValue().stream()
                                .mapToDouble(DetallePedido::getSubtotal)
                                .sum())
                        .build())
                .sorted(Comparator.comparing(VentaPorDiaDTO::getFecha))
                .collect(Collectors.toList());

        return EstadisticasVentasDTO.builder()
                .fechaInicio(inicio)
                .fechaFin(fin)
                .totalPedidos(totalPedidos)
                .ventaPromedioDiaria(Math.round(ventaPromedio * 100.0) / 100.0)
                .productoMasVendido(productoTop != null ? productoTop.getKey() : "N/A")
                .saborMasVendido(saborTop != null ? saborTop.getKey() : "N/A")
                .ventasPorDia(ventasPorDia)
                .build();
    }

    /**
     * Limpia predicciones antiguas
     */
    @Transactional
    public void limpiarPrediccionesAntiguas() {
        LocalDate hace30Dias = LocalDate.now().minusDays(30);
        prediccionRepository.deleteByFechaPrediccionBefore(hace30Dias);
        log.info("Predicciones anteriores a {} eliminadas", hace30Dias);
    }
}
