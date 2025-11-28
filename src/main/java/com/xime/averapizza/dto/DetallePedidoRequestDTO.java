package com.xime.averapizza.dto;

import lombok.Data;

@Data
public class DetallePedidoRequestDTO {
    private Long productoId;
    private Long presentacionId;
//    private Long sabor1Id;
//    private Long sabor2Id;  // opcional
//    private Long sabor3Id;  // opcional
//    private Double pesoKg;  // solo PESO
    private Integer cantidad;
}

