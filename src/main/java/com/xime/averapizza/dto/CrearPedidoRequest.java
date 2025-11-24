package com.xime.averapizza.dto;

import lombok.Data;
import java.util.List;

@Data
public class CrearPedidoRequest {

    private Integer usuarioId; // Cajero
    private String tipoServicio; // MESA, LLEVAR, DELIVERY
    private List<DetallePedidoRequestDTO> detalles;
}
