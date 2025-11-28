package com.xime.averapizza.controller;

import com.xime.averapizza.model.PresentacionProducto;
import com.xime.averapizza.model.PresentacionSabor;
import com.xime.averapizza.model.SaborPizza;
import com.xime.averapizza.repository.PresentacionProductoRepository;
import com.xime.averapizza.repository.PresentacionSaborRepository;
import com.xime.averapizza.repository.SaborPizzaRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/presentacion-sabor")
@RequiredArgsConstructor
@Tag(name = "Combinaciones de Presentación y Sabores")
public class PresentacionSaborController {

//    private final PresentacionSaborRepository presentacionSaborRepo;
//    private final PresentacionProductoRepository presentacionRepo;
//    private final SaborPizzaRepository saborRepo;
//
//    @GetMapping
//    public List<PresentacionSabor> listar() {
//        return presentacionSaborRepo.findAll();
//    }
//
//    @GetMapping("/presentacion/{presentacionId}")
//    public List<PresentacionSabor> listarPorPresentacion(@PathVariable Long presentacionId) {
//        return presentacionSaborRepo.findByPresentacionId(presentacionId);
//    }
//
//    @PostMapping
//    public PresentacionSabor crear(@RequestBody PresentacionSabor ps) {
//
//        PresentacionProducto presentacion = presentacionRepo.findById(ps.getPresentacion().getId())
//                .orElseThrow(() -> new RuntimeException("Presentación no encontrada"));
//
//        SaborPizza sabor = saborRepo.findById(ps.getSabor().getId())
//                .orElseThrow(() -> new RuntimeException("Sabor no encontrado"));
//
//        ps.setPresentacion(presentacion);
//        ps.setSabor(sabor);
//
//        return presentacionSaborRepo.save(ps);
//    }
//
//    @DeleteMapping("/{id}")
//    public String eliminar(@PathVariable Long id) {
//        presentacionSaborRepo.deleteById(id);
//        return "Combinación eliminada";
//    }
}
