package com.xime.averapizza.controller;

import com.xime.averapizza.model.Producto;
import com.xime.averapizza.model.SaborPizza;
import com.xime.averapizza.repository.ProductoRepository;
import com.xime.averapizza.repository.SaborPizzaRepository;
import com.xime.averapizza.service.ProductoService;
import com.xime.averapizza.service.SaborPizzaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sabores")
@RequiredArgsConstructor
@Tag(name = "Sabores de Pizza")
public class SaborPizzaController {

    private final SaborPizzaService service;
    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<SaborPizza> crear(@RequestBody SaborPizza req) {
        productoService.obtener(req.getProducto().getId());

        return ResponseEntity.ok(service.crear(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaborPizza> actualizar(@PathVariable Long id, @RequestBody SaborPizza req) {
        return ResponseEntity.ok(service.actualizar(id, req));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<SaborPizza>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(service.listarPorProducto(productoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaborPizza> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }
}

