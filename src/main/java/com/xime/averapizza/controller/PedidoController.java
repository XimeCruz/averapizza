package com.xime.averapizza.controller;

import com.xime.averapizza.dto.CambiarEstadoDTO;
import com.xime.averapizza.dto.CrearPedidoRequest;
import com.xime.averapizza.dto.InsumoCalculadoDTO;
import com.xime.averapizza.dto.PedidoResponseDTO;
import com.xime.averapizza.model.DetallePedido;
import com.xime.averapizza.model.Pedido;
import com.xime.averapizza.service.PedidoService;
import com.xime.averapizza.service.RecetaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cajero/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gestión completa de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    private final RecetaService recetaService;

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

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestBody CambiarEstadoDTO request) {

        try {

            Pedido pedido = pedidoService.cambiarEstado(
                    id,
                    request.getEstado(),
                    request.getUsuarioId()
            );

            return ResponseEntity.ok(pedido);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/preview-insumos")
    public ResponseEntity<?> previewInsumos(@PathVariable Long id) {
        Pedido pedido = pedidoService.obtenerPorId(id);

        List<InsumoCalculadoDTO> todosLosInsumos = new ArrayList<>();

        for (DetallePedido detalle : pedido.getDetalles()) {
            List<InsumoCalculadoDTO> insumos =
                    recetaService.calcularInsumosParaDetalle(detalle);
            todosLosInsumos.addAll(insumos);
        }

        // Agrupar repetidos
        Map<Long, InsumoCalculadoDTO> agrupados = todosLosInsumos.stream()
                .collect(Collectors.toMap(
                        InsumoCalculadoDTO::getInsumoId,
                        dto -> dto,
                        (dto1, dto2) -> {
                            dto1.setCantidadNecesaria(
                                    dto1.getCantidadNecesaria() + dto2.getCantidadNecesaria()
                            );
                            return dto1;
                        }
                ));

        return ResponseEntity.ok(agrupados.values());
    }
}

