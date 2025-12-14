package com.xime.averapizza.controller;

import com.xime.averapizza.dto.CrearRecetaRequest;
import com.xime.averapizza.dto.RecetaDTO;
import com.xime.averapizza.dto.RecetaDetalleRequest;
import com.xime.averapizza.model.Insumo;
import com.xime.averapizza.model.Receta;
import com.xime.averapizza.model.RecetaDetalle;
import com.xime.averapizza.service.RecetaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/recetas")
@RequiredArgsConstructor
@Tag(name = "Recetas", description = "Gestión de recetas de productos")
public class RecetaController {

    private final RecetaService recetaService;

    @PostMapping("/{saborId}")
    public ResponseEntity<RecetaDTO> crearOActualizar(
            @PathVariable Long saborId,
            @RequestBody List<RecetaDetalleRequest> detalles
    ) {
        System.out.println("Iniciando receta " + saborId);
        List<RecetaDetalle> items = detalles.stream().map(d -> {
            RecetaDetalle det = new RecetaDetalle();
            det.setCantidad(d.getCantidad());

            Insumo insumo = new Insumo();
            insumo.setId(d.getInsumoId());
            det.setInsumo(insumo);

            return det;
        }).toList();

        return ResponseEntity.ok(recetaService.crearOActualizar(saborId, items));
    }

    @GetMapping("/{saborId}")
    public ResponseEntity<RecetaDTO> obtener(@PathVariable Long saborId) {
        return ResponseEntity.ok(recetaService.obtenerPorSabor(saborId));
    }

    /**
     * GET /api/recetas
     * Listar todas las recetas
     */
    @GetMapping
    public ResponseEntity<List<RecetaDTO>> listar() {
        return ResponseEntity.ok(recetaService.listarTodas());
    }

    /**
     * GET /api/recetas/sabor/{saborId}
     * Obtener receta de un sabor
     */
    @GetMapping("/sabor/{saborId}")
    public ResponseEntity<RecetaDTO> obtenerPorSabor(@PathVariable Long saborId) {
        try {
            RecetaDTO receta = recetaService.obtenerPorSabor(saborId);
            return ResponseEntity.ok(receta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/recetas/sabor/{saborId}/existe
     * Verificar si un sabor tiene receta
     */
    @GetMapping("/sabor/{saborId}/existe")
    public ResponseEntity<Map<String, Boolean>> verificarSiTieneReceta(@PathVariable Long saborId) {
        boolean existe = recetaService.tieneReceta(saborId);
        return ResponseEntity.ok(Map.of("tieneReceta", existe));
    }

    /**
     * POST /api/recetas
     * Crear nueva receta
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearRecetaRequest request) {
        try {
            RecetaDTO receta = recetaService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(receta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/recetas/{id}
     * Actualizar receta existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody CrearRecetaRequest request) {
        try {
            RecetaDTO receta = recetaService.actualizar(id, request);
            return ResponseEntity.ok(receta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/recetas/{id}
     * Eliminar receta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            recetaService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Receta eliminada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

