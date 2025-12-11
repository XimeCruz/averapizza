package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialPedidoDTO {
    private Long pedidoId;
    private LocalDateTime fecha;
    private Double total;
    private String estado;
    private String tipoServicio;
    private Integer numeroProductos;
}
