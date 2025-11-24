package com.xime.averapizza.service;

import com.xime.averapizza.dto.CrearRecetaRequestDTO;
import com.xime.averapizza.model.Receta;

public interface RecetaService {

    Receta crearOActualizarReceta(CrearRecetaRequestDTO request);
}

