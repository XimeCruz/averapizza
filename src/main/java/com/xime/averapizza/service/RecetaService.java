package com.xime.averapizza.service;

import com.xime.averapizza.dto.CrearRecetaRequest;
import com.xime.averapizza.dto.InsumoCalculadoDTO;
import com.xime.averapizza.dto.RecetaDTO;
import com.xime.averapizza.model.DetallePedido;
import com.xime.averapizza.model.Receta;
import com.xime.averapizza.model.RecetaDetalle;
import com.xime.averapizza.model.SaborPizza;

import java.util.List;

public interface RecetaService {

//    Receta crearOActualizarReceta(CrearRecetaRequest request);
//
    //Receta obtenerPorSabor(SaborPizza sabor);

    RecetaDTO crearOActualizar(Long saborId, List<RecetaDetalle> detalles);
    RecetaDTO obtenerPorSabor(Long saborId);
    List<InsumoCalculadoDTO> calcularInsumosParaDetalle(DetallePedido detalle);

    boolean tieneReceta(Long saborId);

    // Crear receta
    RecetaDTO crear(CrearRecetaRequest request);

    // Actualizar receta
    RecetaDTO actualizar(Long recetaId, CrearRecetaRequest request);

    // Eliminar receta
    void eliminar(Long recetaId);

    // Listar todas las recetas
    List<RecetaDTO> listarTodas();
}

