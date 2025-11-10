package com.xime.averapizza.security.model;

import lombok.Data;

@Data
public class AuthRequest {
    private String correo;
    private String password;
}

