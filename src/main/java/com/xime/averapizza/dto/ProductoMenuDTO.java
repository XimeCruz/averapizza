package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMenuDTO {
    private Long id;
    private String nombre;
    private Double precio;
    private String presentacion; // PESO, REDONDA, BANDEJA, ML_330, etc.
    private Long presentacionId;
    private String tipoProducto; // PIZZA, BEBIDA
}