package com.xime.averapizza.dto;

import com.xime.averapizza.model.EstadoPedido;
import lombok.Data;

@Data
public class CambiarEstadoDTO {

    private EstadoPedido estado;
    private Integer usuarioId;
}
