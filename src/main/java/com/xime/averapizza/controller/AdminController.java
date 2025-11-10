package com.xime.averapizza.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/reportes")
    public String reportes() {
        return "📊 Solo el ADMIN puede ver los reportes generales.";
    }

    @PostMapping("/crear-producto")
    public String crearProducto() {
        return "✅ Producto creado correctamente (solo ADMIN).";
    }
}

