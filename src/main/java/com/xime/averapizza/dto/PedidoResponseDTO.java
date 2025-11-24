package com.xime.averapizza.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PedidoResponseDTO {

    private Long pedidoId;
    private String tipoServicio;
    private String estado;
    //private LocalDateTime fechaHora;
    private Double total;
    private List<DetallePedidoItem> items;
}

