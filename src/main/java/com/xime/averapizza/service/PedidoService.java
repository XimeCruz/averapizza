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

    PedidoResponseDTO obtenerPedidoDTO(Long pedidoId);

    List<PedidoResponseDTO> listarPedidosHoy();

    List<PedidoResponseDTO> listarPorRango(String inicio, String fin);

    List<PedidoResponseDTO> listarPorEstado(String estado);

    PedidoResponseDTO tomarParaPreparacion(Long pedidoId);

    PedidoResponseDTO marcarListo(Long pedidoId);

    PedidoResponseDTO marcarEntregado(Long pedidoId);

    PedidoResponseDTO cancelar(Long pedidoId);

}

