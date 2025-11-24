package com.xime.averapizza.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetallePedidoItem {
    private String producto;
    private Integer cantidad;
    private Double subtotal;
}
