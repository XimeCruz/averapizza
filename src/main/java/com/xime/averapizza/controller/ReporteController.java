package com.xime.averapizza.controller;

import com.xime.averapizza.dto.ReporteDiarioResponse;
import com.xime.averapizza.service.ReporteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
