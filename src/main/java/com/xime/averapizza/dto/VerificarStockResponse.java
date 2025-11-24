package com.xime.averapizza.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificarStockResponse {
    private boolean suficiente;
    private String mensaje;
}

