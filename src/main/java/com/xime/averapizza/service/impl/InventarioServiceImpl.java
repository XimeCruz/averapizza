package com.xime.averapizza.service.impl;

import com.xime.averapizza.model.*;
import com.xime.averapizza.repository.RecetaRepository;
import com.xime.averapizza.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final JdbcTemplate jdbcTemplate;
    private final RecetaRepository recetaRepository;

    @Override
    public void descontarPorSabor(SaborPizza sabor, int cantidad) {

        Receta receta = recetaRepository.findBySabor(sabor)
                .orElseThrow(() ->
                        new RuntimeException("No existe receta para el sabor: " + sabor.getNombre())
                );

        for (RecetaDetalle det : receta.getDetalles()) {

            double total = det.getCantidad() * cantidad;

            ajustarStock(
                    det.getInsumo().getId(),
                    total,
                    "SALIDA",                             // movimiento de salida
                    "VENTA_SABOR_" + sabor.getId(),       // referencia
                    1                                      // usuario admin (luego cambias)
            );
        }
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

