package com.xime.averapizza.controller;

import com.xime.averapizza.model.PresentacionProducto;
import com.xime.averapizza.repository.PresentacionProductoRepository;
import com.xime.averapizza.repository.ProductoRepository;
import com.xime.averapizza.service.PresentacionProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente/presentaciones")
@RequiredArgsConstructor
@Tag(name = "Presentaciones de Producto")
public class PresentacionProductoController {

    private final PresentacionProductoRepository presentacionRepo;
    private final PresentacionProductoService service;

    @PostMapping
    public ResponseEntity<PresentacionProducto> crear(@RequestBody PresentacionProducto req) {
        return ResponseEntity.ok(service.crear(req));
    }

    @GetMapping
    public ResponseEntity<List<PresentacionProducto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresentacionProducto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        presentacionRepo.deleteById(id);
        return "Presentación eliminada";
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<PresentacionProducto>> obtenerPresentacionesPorProducto(
            @PathVariable Integer productoId) {

        List<PresentacionProducto> lista = service.obtenerPorProductoId(productoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }
}
