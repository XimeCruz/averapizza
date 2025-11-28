package com.xime.averapizza.controller;

import com.xime.averapizza.dto.InventarioAjusteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioAjusteController {

    private final JdbcTemplate jdbcTemplate;

    @PostMapping("/ajustar")
    public String ajustar(@RequestBody InventarioAjusteRequest req) {

//        jdbcTemplate.queryForObject(
//                "SELECT fn_ajustar_stock(?, ?, ?, ?, ?)",
//                Void.class,
//                req.getInsumoId(),
//                req.getCantidad(),
//                req.getTipoMov(),
//                req.getReferencia(),
//                req.getUsuarioId()
//        );

        jdbcTemplate.update(
                "CALL fn_ajustar_stock(?, ?, ?, ?, ?);",
                req.getInsumoId(),
                req.getCantidad(),
                req.getTipoMov(),
                req.getReferencia(),
                req.getUsuarioId()
        );

        return "Ajuste aplicado";
    }
}
