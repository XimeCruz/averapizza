package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrediccionRequestDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long productoId; // opcional, null = todos
    private String tipoPrediccion; // DIA_SEMANA, TENDENCIA, etc.
}



