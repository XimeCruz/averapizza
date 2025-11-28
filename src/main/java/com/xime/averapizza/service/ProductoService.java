package com.xime.averapizza.service;

import com.xime.averapizza.model.Producto;

import java.util.List;

public interface ProductoService {
    Producto crear(Producto producto);
    Producto actualizar(Long id, Producto p);
    Producto obtener(Long id);
    List<Producto> listar();
    void eliminar(Long id);
}

