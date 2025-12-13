package com.xime.averapizza.controller;

import com.xime.averapizza.dto.InventarioAjusteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioAjusteController {

    private final JdbcTemplate jdbcTemplate;

    @PostMapping("/ajustar")
    public ResponseEntity<String> ajustar(@RequestBody InventarioAjusteRequest req) {
        try {
            Integer resultado = jdbcTemplate.queryForObject(
                    "SELECT fn_ajustar_stock(?, ?, ?::tipo_movimiento, ?, ?)",
                    Integer.class,
                    req.getInsumoId(),
                    req.getCantidad(),
                    req.getTipoMov(),
                    req.getReferencia(),
                    req.getUsuarioId()
            );

            return ResponseEntity.ok("Ajuste aplicado exitosamente al insumo: " + resultado);

        } catch (DataAccessException e) {
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getCause().getMessage());
        }
    }
}
