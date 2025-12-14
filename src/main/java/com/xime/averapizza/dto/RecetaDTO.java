package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecetaDTO {
    private Long id;
    private Long saborId;
    private String saborNombre;
    private boolean activo;
    private List<RecetaDetalleDTO> detalles;
}

