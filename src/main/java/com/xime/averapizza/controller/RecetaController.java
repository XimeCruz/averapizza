package com.xime.averapizza.controller;

import com.xime.averapizza.dto.CrearRecetaRequestDTO;
import com.xime.averapizza.model.Producto;
import com.xime.averapizza.model.Receta;
import com.xime.averapizza.repository.ProductoRepository;
import com.xime.averapizza.repository.RecetaRepository;
import com.xime.averapizza.service.RecetaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/recetas")
@RequiredArgsConstructor
@Tag(name = "Recetas", description = "Gestión de recetas de productos")
public class RecetaController {

    private final RecetaService recetaService;
    private final RecetaRepository recetaRepository;
    private final ProductoRepository productoRepository;

    @PostMapping
    public Receta crearOActualizarReceta(@RequestBody CrearRecetaRequestDTO request) {
        return recetaService.crearOActualizarReceta(request);
    }

    @GetMapping("/{productoId}")
    public Receta obtenerPorProducto(@PathVariable Long productoId) {
        Producto p = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return recetaRepository.findByProducto(p)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));
    }

    @PutMapping("/{recetaId}")
    public Receta actualizar(@PathVariable Integer recetaId,
                             @RequestBody CrearRecetaRequestDTO request) {

        request.setProductoId(request.getProductoId());
        return recetaService.crearOActualizarReceta(request);
    }
}
