package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoCompletoDTO {
    private Long productoId;
    private String producto;
    private String tipoProducto;
    private String sabor;
    private String presentacion;
    private Double precio;
    private Long saborId;
    private Long presentacionId;
}