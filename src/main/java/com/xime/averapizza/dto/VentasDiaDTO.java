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
public class VentasDiaDTO {
    private LocalDate fecha;
    private Double totalVentas;
    private Integer numeroVentas;
    private Double promedioVenta;
}
