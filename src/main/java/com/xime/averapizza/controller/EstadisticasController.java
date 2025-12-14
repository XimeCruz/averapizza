package com.xime.averapizza.controller;

import com.xime.averapizza.dto.SaborVentasDTO;
import com.xime.averapizza.service.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    /**
     * Obtiene todos los sabores vendidos con desglose por presentación
     *
     * @param fechaInicio Fecha inicial (opcional)
     * @param fechaFin Fecha final (opcional)
     * @return Lista de sabores con sus ventas por presentación
     */
    @GetMapping("/sabores-mas-vendidos")
    public ResponseEntity<List<SaborVentasDTO>> obtenerSaboresMasVendidos(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaFin
    ) {
        List<SaborVentasDTO> sabores = estadisticasService.obtenerSaboresMasVendidos(fechaInicio, fechaFin);
        return ResponseEntity.ok(sabores);
    }

    /**
     * Obtiene el top 10 de sabores más vendidos
     *
     * @param fechaInicio Fecha inicial (opcional)
     * @param fechaFin Fecha final (opcional)
     * @return Top 10 de sabores
     */
    @GetMapping("/top-sabores")
    public ResponseEntity<List<SaborVentasDTO>> obtenerTop10Sabores(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaFin
    ) {
        List<SaborVentasDTO> topSabores = estadisticasService.obtenerTop10SaboresMasVendidos(fechaInicio, fechaFin);
        return ResponseEntity.ok(topSabores);
    }
}
