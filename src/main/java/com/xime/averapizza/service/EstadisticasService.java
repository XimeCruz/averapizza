package com.xime.averapizza.service;

import com.xime.averapizza.dto.SaborVentasDTO;
import com.xime.averapizza.repository.SaborPizzaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstadisticasService {

    private final SaborPizzaRepository saborPizzaRepository;

    @Transactional(readOnly = true)
    public List<SaborVentasDTO> obtenerSaboresMasVendidos(LocalDateTime fechaInicio, LocalDateTime fechaFin) {

        List<Object[]> resultados = saborPizzaRepository.findSaboresVentasPorPresentacion(fechaInicio, fechaFin);

        // Agrupar por sabor
        Map<Long, SaborVentasDTO> saboresMap = new HashMap<>();

        for (Object[] row : resultados) {
            Long saborId = ((Number) row[0]).longValue();
            String saborNombre = (String) row[1];
            Long presentacionId = row[2] != null ? ((Number) row[2]).longValue() : null;
            String tipoPresentacion = row[3] != null ? row[3].toString() : "SIN_PRESENTACION";
            Double cantidadVendida = row[4] != null ? ((Number) row[4]).doubleValue() : 0.0;
            Integer numeroVentas = row[5] != null ? ((Number) row[5]).intValue() : 0;
            Double ingresoTotal = row[6] != null ? ((Number) row[6]).doubleValue() : 0.0;

            // Obtener o crear el DTO del sabor
            SaborVentasDTO saborDTO = saboresMap.computeIfAbsent(saborId, id ->
                    SaborVentasDTO.builder()
                            .saborId(saborId)
                            .saborNombre(saborNombre)
                            .totalVendido(0.0)
                            .cantidadPedidos(0)
                            .detallesPorPresentacion(new ArrayList<>())
                            .build()
            );

            // Agregar detalle de presentación
            SaborVentasDTO.VentaPorPresentacion detalle = SaborVentasDTO.VentaPorPresentacion.builder()
                    .presentacionId(presentacionId)
                    .tipoPresentacion(tipoPresentacion)
                    .cantidadVendida(cantidadVendida)
                    .numeroVentas(numeroVentas)
                    .ingresoTotal(ingresoTotal)
                    .build();

            saborDTO.getDetallesPorPresentacion().add(detalle);
            saborDTO.setTotalVendido(saborDTO.getTotalVendido() + ingresoTotal);
            saborDTO.setCantidadPedidos(saborDTO.getCantidadPedidos() + numeroVentas);
        }

        // Convertir a lista y ordenar por total vendido
        return saboresMap.values().stream()
                .sorted((s1, s2) -> Double.compare(s2.getTotalVendido(), s1.getTotalVendido()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SaborVentasDTO> obtenerTop10SaboresMasVendidos(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return obtenerSaboresMasVendidos(fechaInicio, fechaFin).stream()
                .limit(10)
                .collect(Collectors.toList());
    }
}
