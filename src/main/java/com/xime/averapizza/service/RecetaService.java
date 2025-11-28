package com.xime.averapizza.service;

import com.xime.averapizza.dto.CrearRecetaRequest;
import com.xime.averapizza.model.Receta;
import com.xime.averapizza.model.RecetaDetalle;
import com.xime.averapizza.model.SaborPizza;

import java.util.List;

public interface RecetaService {

//    Receta crearOActualizarReceta(CrearRecetaRequest request);
//
    //Receta obtenerPorSabor(SaborPizza sabor);

    Receta crearOActualizar(Long saborId, List<RecetaDetalle> detalles);
    Receta obtenerPorSabor(Long saborId);
}

