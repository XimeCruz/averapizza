package com.xime.averapizza.dto;

import lombok.Data;

@Data
public class DetallePedidoRequestDTO {
    private Long productoId;
    private Integer cantidad;
}

