package com.xime.averapizza.service;

import com.xime.averapizza.model.PrecioSaborPresentacion;

import java.util.List;

public interface PrecioSaborPresentacionService {
    PrecioSaborPresentacion asignarPrecio(Long saborId, Long presentacionId, Double precio);
    List<PrecioSaborPresentacion> listarPorSabor(Long saborId);
}

