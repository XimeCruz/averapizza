package com.xime.averapizza.controller;

import com.xime.averapizza.dto.CrearPedidoRequest;
import com.xime.averapizza.dto.PedidoResponseDTO;
import com.xime.averapizza.service.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cajero/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gestión completa de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    // === 🟦 CAJERO: Crear Pedido ===
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido(@RequestBody CrearPedidoRequest request) {
        return ResponseEntity.ok(pedidoService.crearPedido(request));
    }

    // === 🟦 GENERAL: Obtener Detalle ===
    @GetMapping("/{pedidoId}")
    public ResponseEntity<PedidoResponseDTO> obtener(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoDTO(pedidoId));
    }

    // === 🟦 GENERAL: Listar por Estado ===
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pedidoService.listarPorEstado(estado));
    }

    // === 🟦 CAJERO: Cancelar Pedido ===
    @PostMapping("/{pedidoId}/cancelar")
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.cancelar(pedidoId));
    }

    // === 🟦 CAJERO: Listar pedidos del día ===
    @GetMapping("/hoy")
    public ResponseEntity<List<PedidoResponseDTO>> pedidosHoy() {
        System.out.println("Entro");
        return ResponseEntity.ok(pedidoService.listarPedidosHoy());
    }

    // === 🟦 CAJERO: Listar por rango ===
    @GetMapping("/rango")
    public ResponseEntity<List<PedidoResponseDTO>> pedidosPorRango(
            @RequestParam String inicio,
            @RequestParam String fin
    ) {
        return ResponseEntity.ok(pedidoService.listarPorRango(inicio, fin));
    }
}

