package com.xime.averapizza.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @GetMapping("/menu")
    public String verMenu() {
        return "🍕 Menú disponible para todos los CLIENTES.";
    }

    @PostMapping("/pedido")
    public String hacerPedido() {
        return "🧾 Pedido realizado exitosamente (CLIENTE autenticado).";
    }
}

