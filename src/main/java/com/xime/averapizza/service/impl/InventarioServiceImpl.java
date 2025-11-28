package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.VerificarStockRequestDTO;
import com.xime.averapizza.dto.VerificarStockResponse;
import com.xime.averapizza.model.*;
import com.xime.averapizza.repository.InsumoRepository;
import com.xime.averapizza.repository.ProductoRepository;
import com.xime.averapizza.repository.RecetaDetalleRepository;
import com.xime.averapizza.repository.RecetaRepository;
import com.xime.averapizza.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class InventarioServiceImpl implements InventarioService {
//
//    private final ProductoRepository productoRepository;
//    private final RecetaDetalleRepository recetaDetalleRepository;
//    private final InsumoRepository insumoRepository;
//    private final JdbcTemplate jdbcTemplate;
//
//    @Override
//    public VerificarStockResponse verificarStockParaProducto(VerificarStockRequestDTO request) {
//
//        Producto producto = productoRepository.findById(request.getProductoId())
//                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
//
//        if (Boolean.FALSE.equals(producto.getConReceta())) {
//            return VerificarStockResponse.builder()
//                    .suficiente(true)
//                    .mensaje("El producto no usa receta, no se valida stock por ingredientes.")
//                    .build();
//        }
//
//        List<RecetaDetalle> detalles = recetaDetalleRepository.findByProducto(producto);
//
//        StringBuilder msg = new StringBuilder();
//        boolean todoOk = true;
//
//        for (RecetaDetalle det : detalles) {
//            Insumo insumo = det.getInsumo();
//
//            double requerido = det.getCantidad() * request.getCantidad();
//            double disponible = insumo.getStockActual() != null ? insumo.getStockActual() : 0.0;
//
//            if (disponible < requerido) {
//                todoOk = false;
//                msg.append(String.format("Insumo %s insuficiente. Necesario: %.2f %s, disponible: %.2f %s. ",
//                        insumo.getNombre(), requerido, insumo.getUnidadMedida(), disponible, insumo.getUnidadMedida()));
//            }
//        }
//
//        if (todoOk) {
//            return VerificarStockResponse.builder()
//                    .suficiente(true)
//                    .mensaje("Stock suficiente para el producto.")
//                    .build();
//        } else {
//            return VerificarStockResponse.builder()
//                    .suficiente(false)
//                    .mensaje(msg.toString())
//                    .build();
//        }
//    }
//
//    @Override
//    public void descontarStockPorProducto(Producto producto, Integer cantidadProducto, Integer usuarioId, String referencia) {
//
//        if (Boolean.FALSE.equals(producto.getConReceta())) {
//            return;
//        }
//
//        List<RecetaDetalle> detalles = recetaDetalleRepository.findByProducto(producto);
//
//        for (RecetaDetalle det : detalles) {
//            Insumo insumo = det.getInsumo();
//
//            double cantidadTotal = det.getCantidad() * cantidadProducto;
//
//            // Llamar a fn_ajustar_stock: SALIDA
//            jdbcTemplate.queryForObject(
//                    "SELECT fn_ajustar_stock(?, ?, ?, ?, ?)",
//                    Void.class,
//                    insumo.getId(),
//                    cantidadTotal,
//                    "SALIDA",
//                    referencia,
//                    usuarioId
//            );
//
//            // Opcional: refrescar insumo en memoria
//            Insumo refreshed = insumoRepository.findById(insumo.getId()).orElse(insumo);
//            insumo.setStockActual(refreshed.getStockActual());
//        }
//    }
//
//    @Override
//    public void devolverStockPorProducto(Producto producto, Integer cantidad, Integer usuarioId, String referencia) {
//
//        if (!producto.getConReceta()) return;
//
//        List<RecetaDetalle> detalles = recetaDetalleRepository.findByProducto(producto);
//
//        for (RecetaDetalle det : detalles) {
//
//            double cantidadTotal = det.getCantidad() * cantidad;
//
//            jdbcTemplate.queryForObject(
//                    "SELECT fn_ajustar_stock(?, ?, ?, ?, ?)",
//                    Void.class,
//                    det.getInsumo().getId(),
//                    cantidadTotal,
//                    "ENTRADA",
//                    referencia,
//                    usuarioId
//            );
//        }
//    }
//
////    public void descontarPorSabor(SaborPizza sabor, int cantidad) {
////        Receta receta = recetaRepository.findBySabor(sabor)
////                .orElseThrow(() -> new RuntimeException("Receta no encontrada para sabor: "+sabor.getNombreSabor()));
////
////        for (RecetaDetalle det : receta.getDetalles()) {
////            double total = det.getCantidad() * cantidad;
////            ajustarStock(det.getInsumo().getId(), total, "SALIDA", "VENTA_SABOR", 1);
////        }
////    }
//
//
//}

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

    @Override
    public void ajustarStock(Long insumoId, Double cantidad, String tipo, String referencia, Integer usuarioId) {
        jdbcTemplate.update(
                "CALL fn_ajustar_stock(?, ?, ?, ?, ?);",
                insumoId,
                cantidad,
                tipo,
                referencia,
                usuarioId
        );

    }
}

