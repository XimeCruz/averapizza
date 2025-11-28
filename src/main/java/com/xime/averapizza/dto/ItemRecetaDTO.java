package com.xime.averapizza.dto;

import lombok.Data;

@Data
public class ItemRecetaDTO {

    private Long insumoId;      // ID del insumo
    private Double cantidad;    // Cantidad usada por UNA pizza del sabor

}
