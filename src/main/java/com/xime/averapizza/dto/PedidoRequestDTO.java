package com.xime.averapizza.dto;

import lombok.Data;
import java.util.List;

@Data
public class PedidoRequestDTO {

    private String tipoServicio; // "En mesa", "Delivery", "Para llevar"
    private List<DetallePedidoRequestDTO> detalles;
}
