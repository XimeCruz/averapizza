package com.xime.averapizza.controller;

import com.xime.averapizza.dto.VerificarStockRequestDTO;
import com.xime.averapizza.dto.VerificarStockResponse;
import com.xime.averapizza.service.InventarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/inventario")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Verificación y ajustes de inventario")
public class InventarioController {

    private final InventarioService inventarioService;

//    @PostMapping("/verificar-stock")
//    public VerificarStockResponse verificarStock(@RequestBody VerificarStockRequestDTO request) {
//        return inventarioService..verificarStockParaProducto(request);
//    }
}
