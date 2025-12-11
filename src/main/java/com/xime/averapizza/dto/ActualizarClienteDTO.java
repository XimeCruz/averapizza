package com.xime.averapizza.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarClienteDTO {
    private String nombre;
    private String correo;
}