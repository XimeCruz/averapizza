package com.xime.averapizza.dto;

import lombok.Data;

@Data
public class DetallePedidoPizzaDTO {
    private Long presentacionId;  // PESO / REDONDA / BANDEJA
    private Long sabor1Id;
    private Long sabor2Id;        // opcional
    private Long sabor3Id;        // opcional
    private Double pesoKg;        // solo si presentacion == PESO
    private Integer cantidad;
}
