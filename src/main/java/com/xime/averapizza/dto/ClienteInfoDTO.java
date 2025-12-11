package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteInfoDTO {
    private Long id;
    private String nombre;
    private String correo;
    private Boolean activo;
    private Long totalPedidos;
    private Double totalGastado;
}