package com.xime.averapizza.dto;

import lombok.Data;
import java.util.List;

@Data
public class CrearRecetaRequest {

    private Long saborId;
    private List<DetalleInsumoRequest> detalles;

}

