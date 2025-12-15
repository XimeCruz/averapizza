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
public class PrediccionDetalladaDTO {
    private LocalDate fecha;
    private List<PrediccionItemDTO> items;
    private Integer totalPedidos;
    private Double confianzaPromedio;
}
