package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoTopDTO {
    private String nombre;
    private Long totalVendido;
}

