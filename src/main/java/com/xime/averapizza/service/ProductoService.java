package com.xime.averapizza.service;

import com.xime.averapizza.dto.ProductoCompletoDTO;
import com.xime.averapizza.dto.ProductoMenuDTO;
import com.xime.averapizza.model.Producto;

import java.util.List;
import java.util.Map;

public interface ProductoService {
    Producto crear(Producto producto);
    Producto actualizar(Long id, Producto p);
    Producto obtener(Long id);
    List<Producto> listar();
    void eliminar(Long id);

    List<ProductoCompletoDTO> obtenerTodosLosProductos();

    List<ProductoCompletoDTO> obtenerProductosPorTipo(String tipo);

    Map<String, List<ProductoMenuDTO>> obtenerPizzasPorPresentacion();

    List<ProductoMenuDTO> obtenerBebidas();
}

