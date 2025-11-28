package com.xime.averapizza.controller;

import com.xime.averapizza.model.PresentacionSabor;
import com.xime.averapizza.repository.PresentacionProductoRepository;
import com.xime.averapizza.repository.PresentacionSaborRepository;
import com.xime.averapizza.repository.SaborPizzaRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/pizza-builder")
@RequiredArgsConstructor
@Tag(name = "Pizza Builder (Flutter)")
public class PizzaBuilderController {

    private final PresentacionProductoRepository presentacionRepo;
    private final SaborPizzaRepository saborRepo;
    private final PresentacionSaborRepository presentacionSaborRepo;

//    @GetMapping("/producto/{productoId}")
//    public Map<String, Object> obtenerFlujoPizza(@PathVariable Long productoId) {
//
//        Map<String, Object> resp = new HashMap<>();
//
//        resp.put("presentaciones",
//                presentacionRepo.findByProductoId(productoId));
//
//        resp.put("sabores",
//                saborRepo.findByProductoId(productoId));
//
//        return resp;
//    }

    @GetMapping("/presentacion/{id}/sabores-permitidos")
    public List<PresentacionSabor> saboresPermitidos(@PathVariable Long id) {
        return presentacionSaborRepo.findByPresentacionId(id);
    }
}

