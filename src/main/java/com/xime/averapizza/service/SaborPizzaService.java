package com.xime.averapizza.service;

import com.xime.averapizza.model.SaborPizza;

import java.util.List;

public interface SaborPizzaService {
    SaborPizza crear(SaborPizza sabor);
    SaborPizza actualizar(Long id, SaborPizza sabor);
    List<SaborPizza> listarPorProducto(Long productoId);
    SaborPizza obtener(Long id);
}

