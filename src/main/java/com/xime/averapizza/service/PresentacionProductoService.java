package com.xime.averapizza.service;

import com.xime.averapizza.model.PresentacionProducto;

import java.util.List;

public interface PresentacionProductoService {

    PresentacionProducto crear(PresentacionProducto p);
    List<PresentacionProducto> listar();
    PresentacionProducto obtener(Long id);
    List<PresentacionProducto> obtenerPorProductoId(Integer productoId);
}

