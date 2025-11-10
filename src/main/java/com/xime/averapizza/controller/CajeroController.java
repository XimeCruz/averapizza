package com.xime.averapizza.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cajero")
public class CajeroController {

    @PostMapping("/registrar-venta")
    public String registrarVenta() {
        return "💰 Venta registrada exitosamente (CAJERO o ADMIN).";
    }

    @GetMapping("/historial")
    public String historial() {
        return "📜 Historial de ventas accesible por CAJERO y ADMIN.";
    }
}

