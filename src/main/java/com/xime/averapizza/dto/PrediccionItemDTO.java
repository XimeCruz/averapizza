package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrediccionItemDTO {
    private String productoNombre;
    private String saborNombre;
    private String presentacion;
    private Integer cantidad;
    private Double confianza;
    private HistoricoVentasDTO historico;
}