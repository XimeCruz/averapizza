package com.xime.averapizza.dto;

import lombok.Data;
import java.util.List;

@Data
public class CrearRecetaRequest {

    private Long saborId;   // El sabor al que pertenece esta receta

    private List<ItemRecetaDTO> detalles; // Lista de insumos + cantidades

}

