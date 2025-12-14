package com.xime.averapizza.dto;

import lombok.Data;

@Data
public class DetalleInsumoRequest {
    private Long insumoId;
    private Double cantidad;
    private Long presentacionId;
}
