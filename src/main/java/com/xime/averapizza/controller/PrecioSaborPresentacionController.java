package com.xime.averapizza.controller;

import com.xime.averapizza.model.PrecioSaborPresentacion;
import com.xime.averapizza.service.PrecioSaborPresentacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/precios")
@RequiredArgsConstructor
public class PrecioSaborPresentacionController {

    private final PrecioSaborPresentacionService service;

    @PostMapping
    public ResponseEntity<PrecioSaborPresentacion> asignarPrecio(
            @RequestParam Long saborId,
            @RequestParam Long presentacionId,
            @RequestParam Double precio
    ) {
        return ResponseEntity.ok(service.asignarPrecio(saborId, presentacionId, precio));
    }

    @GetMapping("/sabor/{saborId}")
    public ResponseEntity<List<PrecioSaborPresentacion>> listar(@PathVariable Long saborId) {
        return ResponseEntity.ok(service.listarPorSabor(saborId));
    }
}
