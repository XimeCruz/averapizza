package com.xime.averapizza.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;

    private String nombreUsuario;

    private String correo;

    private String rol;

}

