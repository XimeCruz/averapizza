package com.xime.averapizza.controller;

import com.xime.averapizza.dto.RecetaDTO;
import com.xime.averapizza.dto.RecetaDetalleRequest;
import com.xime.averapizza.model.Insumo;
import com.xime.averapizza.model.Receta;
import com.xime.averapizza.model.RecetaDetalle;
import com.xime.averapizza.service.RecetaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Receta> obtener(@PathVariable Long saborId) {
        return ResponseEntity.ok(recetaService.obtenerPorSabor(saborId));
    }
}

