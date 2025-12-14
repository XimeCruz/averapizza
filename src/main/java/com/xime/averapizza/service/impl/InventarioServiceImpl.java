package com.xime.averapizza.service.impl;

import com.xime.averapizza.exception.StockInsuficienteException;
import com.xime.averapizza.model.*;
import com.xime.averapizza.repository.RecetaRepository;
import com.xime.averapizza.repository.SaborPizzaRepository;
import com.xime.averapizza.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final JdbcTemplate jdbcTemplate;
    private final RecetaRepository recetaRepository;
    private final SaborPizzaRepository saborPizzaRepository;

    @Override
    public void descontarPorSabor(SaborPizza sabor, int cantidad) {

        // Cargar el sabor con su receta
        SaborPizza saborConReceta = saborPizzaRepository.findById(sabor.getId())
                .orElseThrow(() -> new RuntimeException("Sabor no encontrado"));

        if (saborConReceta.getReceta() == null) {
            throw new RuntimeException("El sabor '" + saborConReceta.getNombre() + "' no tiene receta configurada");
        }

        // Validar stock ANTES de descontar
        for (RecetaDetalle detalle : saborConReceta.getReceta().getDetalles()) {
            Insumo insumo = detalle.getInsumo();
            Double cantidadRequerida = detalle.getCantidad() * cantidad;
            try {
                // Llamar a la función de PostgreSQL
                ajustarStock(
                        insumo.getId(),
                        cantidadRequerida,
                        "SALIDA",
                        "Pedido - Sabor: " + saborConReceta.getNombre(),
                        null
                );
            } catch (Exception e) {
                // Parsear el error de PostgreSQL
                throw parseStockException(e, insumo.getNombre());
            }

//            if (!insumo.tieneStockSuficiente(cantidadRequerida)) {
//                throw new StockInsuficienteException(
//                        insumo.getNombre(),
//                        insumo.getStockActual(),
//                        cantidadRequerida
//                );
//            }
        }

//        // Si hay stock suficiente, descontar
//        for (RecetaDetalle detalle : saborConReceta.getReceta().getDetalles()) {
//            Insumo insumo = detalle.getInsumo();
//            Double cantidadADescontar = detalle.getCantidad() * cantidad;
//
//            try {
//                ajustarStock(
//                        insumo.getId(),
//                        cantidadADescontar,
//                        "SALIDA",
//                        "Pedido - Sabor: " + saborConReceta.getNombre(),
//                        null
//                );
//            } catch (Exception e) {
//                String mensaje = e.getMessage();
//                if (mensaje != null && mensaje.contains("Stock insuficiente")) {
//                    throw new StockInsuficienteException(
//                            insumo.getNombre(),
//                            insumo.getStockActual(),
//                            cantidadADescontar
//                    );
//                }
//                throw new RuntimeException("Error al descontar inventario: " + mensaje, e);
//            }
//        }
    }

    private RuntimeException parseStockException(Exception e, String insumoNombre) {
        String mensaje = e.getMessage();

        if (mensaje == null) {
            return new RuntimeException("Error desconocido al ajustar inventario");
        }

        // Verificar si es error de stock insuficiente
        if (mensaje.contains("Stock insuficiente")) {
            // Extraer valores usando regex
            // Formato: Stock insuficiente del insumo "Nombre". Actual: X, Solicitado: Y
            Pattern pattern = Pattern.compile(
                    "Stock insuficiente del insumo \"([^\"]+)\". Actual: ([0-9.]+), Solicitado: ([0-9.]+)"
            );
            Matcher matcher = pattern.matcher(mensaje);

            if (matcher.find()) {
                String nombre = matcher.group(1);
                Double stockActual = Double.parseDouble(matcher.group(2));
                Double stockRequerido = Double.parseDouble(matcher.group(3));

                return new StockInsuficienteException(nombre, stockActual, stockRequerido);
            }

            // Si no se puede parsear, usar el nombre que tenemos
            return new StockInsuficienteException(insumoNombre, 0.0, 0.0);
        }

        // Para otros errores
        if (mensaje.contains("ERROR:")) {
            int errorIndex = mensaje.indexOf("ERROR:");
            int whereIndex = mensaje.indexOf("Where:");

            if (errorIndex != -1) {
                if (whereIndex != -1) {
                    mensaje = mensaje.substring(errorIndex + 7, whereIndex).trim();
                } else {
                    mensaje = mensaje.substring(errorIndex + 7).trim();
                }
            }
        }

        return new RuntimeException(mensaje);
    }


    @Override
    public void devolverPorSabor(SaborPizza sabor, int cantidad) {

        Receta receta = recetaRepository.findBySabor(sabor)
                .orElseThrow(() ->
                        new RuntimeException("No existe receta para el sabor: " + sabor.getNombre())
                );

        for (RecetaDetalle det : receta.getDetalles()) {

            double total = det.getCantidad() * cantidad;

            ajustarStock(
                    det.getInsumo().getId(),
                    total,
                    "ENTRADA",
                    "DEV_PEDIDO_SABOR_" + sabor.getId(),
                    1
            );
        }
    }

//    @Override
//    public void ajustarStock(Long insumoId, Double cantidad, String tipo, String referencia, Integer usuarioId) {
//        jdbcTemplate.update(
//                "CALL fn_ajustar_stock(?, ?, ?, ?, ?);",
//                insumoId,
//                cantidad,
//                tipo,
//                referencia,
//                usuarioId
//        );
//
//    }

    @Override
    public void ajustarStock(Long insumoId, Double cantidad, String tipoMov, String referencia, Integer usuarioId) {
        try {
            // Usar queryForObject en lugar de update
            Integer movimientoId = jdbcTemplate.queryForObject(
                    "SELECT fn_ajustar_stock(?, ?, ?::tipo_movimiento, ?, ?)",
                    Integer.class,
                    insumoId,
                    cantidad,
                    tipoMov,
                    referencia != null ? referencia : "Sistema",
                    usuarioId
            );

            //log.info("Ajuste de stock exitoso. Movimiento ID: {}", movimientoId);

        } catch (DataAccessException e) {
            String errorMsg = e.getCause() != null ?
                    e.getCause().getMessage() : e.getMessage();
            //log.error("Error al ajustar stock: {}", errorMsg);
            throw new RuntimeException("Error al ajustar inventario: " + errorMsg);
        }
    }
}

