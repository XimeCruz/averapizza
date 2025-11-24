package com.xime.averapizza.controller;

import com.xime.averapizza.model.Producto;
import com.xime.averapizza.repository.ProductoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos del menú")
public class ProductoController {

    private final ProductoRepository productoRepository;

    @GetMapping
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @PostMapping
    public Producto crear(@RequestBody Producto p) {
        return productoRepository.save(p);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto p) {
        Producto original = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        original.setNombre(p.getNombre());
        original.setPrecio(p.getPrecio());
        original.setConReceta(p.getConReceta());
        return productoRepository.save(original);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        productoRepository.deleteById(id);
        return "Producto eliminado";
    }
}
