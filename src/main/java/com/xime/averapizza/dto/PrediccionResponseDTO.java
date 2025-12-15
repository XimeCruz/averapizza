package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrediccionResponseDTO {
    private Long id;
    private LocalDate fecha;
    private String productoNombre;
    private String saborNombre;
    private String presentacion;
    private Integer cantidadPredicha;
    private Double confianza;
    private String tipoPrediccion;
}
