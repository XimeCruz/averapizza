package com.xime.averapizza.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoMasVendido {
    private String nombre;
    private Integer cantidad;
}

