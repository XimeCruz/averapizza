package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadosPedidosDTO {
    private Integer pendientes;
    private Integer enPreparacion;
    private Integer listos;
    private Integer entregados;
    private Integer cancelados;
}
