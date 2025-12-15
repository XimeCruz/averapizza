package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoVentasDTO {
    private Double promedioUltimos7Dias;
    private Double promedioUltimos30Dias;
    private Integer ventasAyer;
    private String tendencia; // CRECIENTE, DECRECIENTE, ESTABLE
}