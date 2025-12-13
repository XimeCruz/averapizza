package com.xime.averapizza.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetallePedidoItem {
    private Integer id;
    private Integer pedidoId;
    private Integer productoId;
    private Integer cantidad;
    private Double subtotal;
    private String presentacion;
    private String sabor1;
    private String sabor2;
    private String sabor3;
    private String productoNombre;
}
