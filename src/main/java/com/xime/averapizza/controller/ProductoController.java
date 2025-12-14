package com.xime.averapizza.controller;

import com.xime.averapizza.dto.ProductoCompletoDTO;
import com.xime.averapizza.dto.ProductoMenuDTO;
import com.xime.averapizza.model.Producto;
import com.xime.averapizza.repository.ProductoRepository;
import com.xime.averapizza.service.ProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cliente/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos del menú")
public class ProductoController {

    private final ProductoService service;

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto request) {
        return ResponseEntity.ok(service.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/pizzas-por-presentacion")
    public ResponseEntity<Map<String, List<ProductoMenuDTO>>> getPizzasPorPresentacion() {
        return ResponseEntity.ok(service.obtenerPizzasPorPresentacion());
    }

    /**
     * Obtener todas las bebidas con sus presentaciones
     */
    @GetMapping("/bebidas")
    public ResponseEntity<List<ProductoMenuDTO>> getBebidas() {
        return ResponseEntity.ok(service.obtenerBebidas());
    }

    /**
     * Obtener todos los productos activos para el menú
     */
    @GetMapping("/menu")
    public ResponseEntity<Map<String, Object>> getMenu() {
        Map<String, Object> menu = Map.of(
                "pizzas", service.obtenerPizzasPorPresentacion(),
                "bebidas", service.obtenerBebidas()
        );
        return ResponseEntity.ok(menu);
    }

}

