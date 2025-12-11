package com.xime.averapizza.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambiarPasswordDTO {
    private String passwordActual;
    private String passwordNueva;
    private String passwordConfirmacion;
}