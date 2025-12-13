package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsumoCalculadoDTO {
    private Long insumoId;
    private String nombreInsumo;
    private Double cantidadNecesaria;
    private String unidadMedida;
    private Double stockActual;
}
