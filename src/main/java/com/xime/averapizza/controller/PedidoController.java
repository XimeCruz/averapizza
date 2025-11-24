package com.xime.averapizza.controller;

import com.xime.averapizza.dto.CrearPedidoRequest;
import com.xime.averapizza.dto.PedidoResponseDTO;
import com.xime.averapizza.service.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cajero/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gestión completa de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public PedidoResponseDTO crearPedido(@RequestBody CrearPedidoRequest request) {
        return pedidoService.crearPedido(request);
    }

    @PutMapping("/{id}/estado")
    public PedidoResponseDTO cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado
    ) {
        return pedidoService.cambiarEstado(id, estado);
    }

    // Listar pedidos por estado (ej: LISTO, ENTREGADO, CANCELADO)
    @GetMapping
    public List<PedidoResponseDTO> listar(
            @RequestParam(required = false) String estado
    ) {
        return pedidoService.listarPorEstado(estado);
    }

    // Marcar pedido como ENTREGADO (desde cajero)
    @PutMapping("/{id}/entregar")
    public PedidoResponseDTO marcarEntregado(@PathVariable Long id) {
        return pedidoService.marcarEntregado(id);
    }

    // Cancelar pedido
    @PutMapping("/{id}/cancelar")
    public PedidoResponseDTO cancelar(@PathVariable Long id) {
        return pedidoService.cancelar(id);
    }
}
