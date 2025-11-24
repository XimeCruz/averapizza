package com.xime.averapizza.dto;

import lombok.Data;

import java.util.List;

@Data
public class CrearRecetaRequestDTO {

    private Long productoId;
    private List<RecetaDetalleItemDTO> items;
}

