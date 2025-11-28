package com.xime.averapizza.controller;

import com.xime.averapizza.dto.CrearRecetaRequest;
import com.xime.averapizza.model.Receta;
import com.xime.averapizza.model.SaborPizza;
import com.xime.averapizza.repository.SaborPizzaRepository;
import com.xime.averapizza.service.RecetaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/recetas-sabor")
@RequiredArgsConstructor
@Tag(name = "Recetas por Sabor de Pizza")
public class RecetaSaborController {

    private final RecetaService recetaService;
    private final SaborPizzaRepository saborRepo;

//    @GetMapping("/{saborId}")
//    public Receta obtenerPorSabor(@PathVariable Long saborId) {
//        SaborPizza sabor = saborRepo.findById(saborId)
//                .orElseThrow(() -> new RuntimeException("Sabor no encontrado"));
//        return recetaService.obtenerPorSabor(sabor);
//    }
//
//    @PostMapping
//    public Receta crear(@RequestBody CrearRecetaRequest req) {
//        return recetaService.crearOActualizarReceta(req);
//    }
//
//    @PutMapping("/{saborId}")
//    public Receta actualizar(@PathVariable Long saborId, @RequestBody CrearRecetaRequest req) {
//        req.setSaborId(saborId);
//        return recetaService.crearOActualizarReceta(req);
//    }
}

