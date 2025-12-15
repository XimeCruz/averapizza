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
public class VentaPorDiaDTO {
    private LocalDate fecha;
    private String diaSemana;
    private Integer cantidadPedidos;
    private Double totalVentas;
}