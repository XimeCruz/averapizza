package com.xime.averapizza.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasClienteDTO {
    private Long totalPedidos;
    private Double totalGastado;
    private Double promedioGasto;
    private String pizzaFavorita;
}
