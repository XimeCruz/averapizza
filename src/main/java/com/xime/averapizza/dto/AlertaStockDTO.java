package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaStockDTO {
    private Long insumoId;
    private String nombre;
    private Double stockActual;
    private Double stockMinimo;
    private String unidadMedida;
    private String criticidad; // CRÍTICO, BAJO, MEDIO
}
