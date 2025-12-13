package com.xime.averapizza.service;

import com.xime.averapizza.dto.InsumoCalculadoDTO;

import java.util.List;

public interface InsumoService {
    void descontarStock(Long insumoId, Double cantidad, String referencia, Integer usuarioId);
    void verificarStockDisponible(List<InsumoCalculadoDTO> insumos);
}
