package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecetaDetalleDTO {

    private Long id;
    private Long insumoId;
    private String insumoNombre;
    private String unidadMedida;
    private Double cantidad;
    private Long presentacionId;
    private String presentacionTipo;
}
