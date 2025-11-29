package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsumoBajoStockDTO {
    private Long id;
    private String nombre;
    private Double stockActual;
    private Double stockMinimo;
}
