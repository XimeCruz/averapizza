package com.xime.averapizza.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecetaDTO {
    private Long id;
    private Long saborId;
    private String saborNombre;
    private List<RecetaDetalleDTO> detalles;
}

