package com.xime.averapizza.dto;

import lombok.Data;

@Data
public class VerificarStockRequestDTO {

    private Long productoId;
    private Integer cantidad; // cuántas unidades de producto quiero vender
}
