package com.xime.averapizza.service;

import com.xime.averapizza.model.Pedido;
import com.xime.averapizza.model.SaborPizza;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

//@Service
//@RequiredArgsConstructor
//public class InventarioService {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public void ajustarStock(Integer insumoId, Double cantidad, String tipoMov, String referencia, Integer usuarioId) {
//
//        String sql = "SELECT fn_ajustar_stock(?, ?, ?, ?, ?)";
//
//        jdbcTemplate.queryForObject(
//                sql,
//                Void.class,
//                insumoId,
//                cantidad,
//                tipoMov,
//                referencia,
//                usuarioId
//        );
//    }
//}


import com.xime.averapizza.dto.VerificarStockRequestDTO;
import com.xime.averapizza.dto.VerificarStockResponse;
import com.xime.averapizza.model.Producto;

public interface InventarioService {

//    VerificarStockResponse verificarStockParaProducto(VerificarStockRequestDTO request);
//
//    void descontarStockPorProducto(Producto producto, Integer cantidadProducto, Integer usuarioId, String referencia);
//
//    void devolverStockPorProducto(Producto producto, Integer cantidadProducto, Integer usuarioId, String referencia);

    void descontarPorSabor(SaborPizza sabor, int cantidad);

    void devolverPorSabor(SaborPizza sabor, int cantidad);

    void ajustarStock(Long insumoId, Double cantidad, String tipo, String referencia, Integer usuarioId);

}


