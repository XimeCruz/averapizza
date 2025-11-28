package com.xime.averapizza.dto;

import lombok.Data;

@Data
public class CrearDetallePizzaDTO {
    private Long productoId;
    private Long presentacionId;
    private Long sabor1Id;
    private Long sabor2Id;  // opcional
    private Long sabor3Id;  // opcional
    private Double pesoKg;  // solo PESO
    private Integer cantidad;
}

