package com.xime.averapizza.controller;

import com.xime.averapizza.model.Insumo;
import com.xime.averapizza.repository.InsumoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/insumos")
@RequiredArgsConstructor
@Tag(name = "Insumos", description = "Gestión del inventario maestro de insumos")
public class InsumoController {

    private final InsumoRepository insumoRepository;

    @GetMapping
    public List<Insumo> listar() {
        return insumoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Insumo obtener(@PathVariable Long id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado"));
    }

    @PostMapping
    public Insumo crear(@RequestBody Insumo i) {
        return insumoRepository.save(i);
    }

    @PutMapping("/{id}")
    public Insumo actualizar(@PathVariable Long id, @RequestBody Insumo i) {
        Insumo original = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado"));

        original.setNombre(i.getNombre());
        original.setUnidadMedida(i.getUnidadMedida());
        original.setStockActual(i.getStockActual());
        original.setStockMinimo(i.getStockMinimo());
        original.setActivo(i.getActivo());

        return insumoRepository.save(original);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        insumoRepository.deleteById(id);
        return "Insumo eliminado";
    }
}
