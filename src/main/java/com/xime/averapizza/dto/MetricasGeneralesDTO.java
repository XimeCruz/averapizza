package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricasGeneralesDTO {
    private Long totalPedidos;
    private Double tasaEntrega;
    private Long totalProductos;
    private Integer alertasStock;

    private MetricasDiaDTO metricasDia;
}
