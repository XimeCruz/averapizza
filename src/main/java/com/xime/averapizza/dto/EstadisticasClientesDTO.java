package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasClientesDTO {
    private Long totalClientes;
    private Long clientesActivos;
    private Long clientesInactivos;
}
