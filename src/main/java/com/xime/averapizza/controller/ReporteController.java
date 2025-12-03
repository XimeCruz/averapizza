package com.xime.averapizza.controller;

import com.xime.averapizza.dto.*;
import com.xime.averapizza.service.ReporteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/reportes")
@Tag(name = "Reportes", description = "Reportes de administrador")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/diario")
    public ReporteDiarioResponse reporteDiario() {
        return reporteService.reporteDiario();
    }

    @GetMapping("/ventas/hoy")
    public ResponseEntity<List<VentaResumenDTO>> ventasHoy() {
        return ResponseEntity.ok(reporteService.ventasHoy());
    }

    @GetMapping("/ventas")
    public ResponseEntity<List<VentaResumenDTO>> ventasEntreFechas(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin
    ) {
        return ResponseEntity.ok(reporteService.ventasEntreFechas(inicio, fin));
    }

    @GetMapping("/productos/top")
    public ResponseEntity<List<ProductoTopDTO>> productosTop() {
        return ResponseEntity.ok(reporteService.productosTop());
    }

    @GetMapping("/inventario/bajo-stock")
    public ResponseEntity<List<InsumoBajoStockDTO>> bajoStock() {
        return ResponseEntity.ok(reporteService.inventarioBajoStock());
    }

    @GetMapping("/ventas-por-tipo")
    public List<VentasPorTipoDTO> getVentasPorTipo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        return reporteService.ventasPorTipo(inicio, fin);
    }
}
