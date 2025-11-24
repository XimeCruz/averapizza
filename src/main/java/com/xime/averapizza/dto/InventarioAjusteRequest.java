package com.xime.averapizza.dto;

import lombok.Data;

@Data
public class InventarioAjusteRequest {
    private Integer insumoId;
    private Double cantidad;
    private String tipoMov;      // ENTRADA, SALIDA, MERMA, AJUSTE
    private String referencia;
    private Integer usuarioId;
}

