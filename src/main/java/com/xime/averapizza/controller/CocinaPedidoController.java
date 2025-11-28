package com.xime.averapizza.controller;

import com.xime.averapizza.dto.PedidoResponseDTO;
import com.xime.averapizza.service.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cocina/pedidos")
@RequiredArgsConstructor
@Tag(name = "Cocina - Pedidos", description = "Gestión de pedidos desde la cocina")
public class CocinaPedidoController {

    private final PedidoService pedidoService;

    // Lista solo los pendientes para cocina
    @GetMapping("/pendientes")
    public ResponseEntity<List<PedidoResponseDTO>> pendientes() {
        return ResponseEntity.ok(pedidoService.listarPorEstado("PENDIENTE"));
    }

    // Cocina toma un pedido
    @PostMapping("/{pedidoId}/tomar")
    public ResponseEntity<PedidoResponseDTO> tomar(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.tomarParaPreparacion(pedidoId));
    }

    @GetMapping("/en-preparacion")
    public List<PedidoResponseDTO> listarEnPreparacion() {
        return pedidoService.listarPorEstado("EN_PREPARACION");
    }

    // Cocina marca como En Preparación → Listo
    @PostMapping("/{pedidoId}/marcar-listo")
    public ResponseEntity<PedidoResponseDTO> marcarListo(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.marcarListo(pedidoId));
    }

    // Cajero/Cocina marca como entregado
    @PostMapping("/{pedidoId}/entregar")
    public ResponseEntity<PedidoResponseDTO> entregar(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.marcarEntregado(pedidoId));
    }
}

//public class CocinaPedidoController {
//
//    private final PedidoService pedidoService;
//
//    // Listar pedidos PENDIENTE o EN_PREPARACION
//    @GetMapping("/pendientes")
//    public List<PedidoResponseDTO> listarPendientes() {
//        return pedidoService.listarPorEstado("PENDIENTE");
//    }
//
//    @GetMapping("/en-preparacion")
//    public List<PedidoResponseDTO> listarEnPreparacion() {
//        return pedidoService.listarPorEstado("EN_PREPARACION");
//    }
//
//    // Tomar pedido para preparación: PENDIENTE -> EN_PREPARACION
//    @PutMapping("/{id}/tomar")
//    public PedidoResponseDTO tomarParaPreparacion(@PathVariable Long id) {
//        return pedidoService.tomarParaPreparacion(id);
//    }
//
//    // Marcar pedido como LISTO
//    @PutMapping("/{id}/marcar-listo")
//    public PedidoResponseDTO marcarListo(@PathVariable Long id) {
//        return pedidoService.marcarListo(id);
//    }
//}
