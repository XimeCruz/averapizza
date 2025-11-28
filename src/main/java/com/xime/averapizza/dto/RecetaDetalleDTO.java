package com.xime.averapizza.dto;

import lombok.Data;

@Data
public class RecetaDetalleDTO {

    private Long id;          // id del detalle de la receta
    private Long insumoId;    // id del insumo
    private String insumoNombre;
    private Double cantidad;
    private String unidadMedida; // opcional, si la tienes en Insumo
}
