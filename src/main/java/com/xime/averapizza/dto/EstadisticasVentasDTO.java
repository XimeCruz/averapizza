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
public class EstadisticasVentasDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer totalPedidos;
    private Double ventaPromedioDiaria;
    private String productoMasVendido;
    private String saborMasVendido;
    private List<VentaPorDiaDTO> ventasPorDia;
}