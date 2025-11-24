package com.xime.averapizza.service;

import com.xime.averapizza.dto.CrearPedidoRequest;
import com.xime.averapizza.dto.PedidoRequestDTO;
import com.xime.averapizza.dto.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {

//    PedidoResponseDTO crearPedido(PedidoRequestDTO request);
//
//    PedidoResponseDTO confirmarPedido(Long pedidoId);
//
//    PedidoResponseDTO cancelarPedido(Long pedidoId);
//
//    List<PedidoResponseDTO> listarPedidos(String estado); // opcional filtro por estado

    PedidoResponseDTO crearPedido(CrearPedidoRequest request);

    PedidoResponseDTO cambiarEstado(Long pedidoId, String nuevoEstado);


    List<PedidoResponseDTO> listarPorEstado(String estado); // para cocina / cajero

    PedidoResponseDTO tomarParaPreparacion(Long pedidoId);   // PENDIENTE -> EN_PREPARACION

    PedidoResponseDTO marcarListo(Long pedidoId);            // EN_PREPARACION -> LISTO

    PedidoResponseDTO marcarEntregado(Long pedidoId);        // LISTO -> ENTREGADO

    PedidoResponseDTO cancelar(Long pedidoId);               // PENDIENTE / EN_PREPARACION -> CANCELADO

}

