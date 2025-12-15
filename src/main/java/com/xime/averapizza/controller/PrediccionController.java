package com.xime.averapizza.controller;

import com.xime.averapizza.dto.*;
import com.xime.averapizza.service.PrediccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/predicciones")
@RequiredArgsConstructor
public class PrediccionController {

    private final PrediccionService prediccionService;

    /**
     * Genera predicciones para un rango de fechas
     * POST /api/predicciones/generar
     */
    @PostMapping("/generar")
    public ResponseEntity<List<PrediccionDetalladaDTO>> generarPredicciones(
            @RequestBody PrediccionRequestDTO request) {

        // Validaciones
        if (request.getFechaInicio() == null) {
            request.setFechaInicio(LocalDate.now().plusDays(1));
        }
        if (request.getFechaFin() == null) {
            request.setFechaFin(request.getFechaInicio().plusDays(7));
        }
        if (request.getTipoPrediccion() == null) {
            request.setTipoPrediccion("DIA_SEMANA");
        }

        List<PrediccionDetalladaDTO> predicciones =
                prediccionService.generarPredicciones(request);

        return ResponseEntity.ok(predicciones);
    }

    /**
     * Obtiene predicción para mañana
     * GET /api/predicciones/manana
     */
    @GetMapping("/manana")
    public ResponseEntity<PrediccionDetalladaDTO> prediccionManana() {
        PrediccionRequestDTO request = PrediccionRequestDTO.builder()
                .fechaInicio(LocalDate.now().plusDays(1))
                .fechaFin(LocalDate.now().plusDays(1))
                .tipoPrediccion("DIA_SEMANA")
                .build();

        List<PrediccionDetalladaDTO> predicciones =
                prediccionService.generarPredicciones(request);

        return ResponseEntity.ok(predicciones.get(0));
    }

    /**
     * Obtiene predicción para la próxima semana
     * GET /api/predicciones/proxima-semana
     */
    @GetMapping("/proxima-semana")
    public ResponseEntity<List<PrediccionDetalladaDTO>> prediccionProximaSemana() {
        PrediccionRequestDTO request = PrediccionRequestDTO.builder()
                .fechaInicio(LocalDate.now().plusDays(1))
                .fechaFin(LocalDate.now().plusDays(7))
                .tipoPrediccion("DIA_SEMANA")
                .build();

        List<PrediccionDetalladaDTO> predicciones =
                prediccionService.generarPredicciones(request);

        return ResponseEntity.ok(predicciones);
    }

    /**
     * Obtiene estadísticas de ventas históricas
     * GET /api/predicciones/estadisticas?inicio=2025-01-01&fin=2025-01-31
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasVentasDTO> obtenerEstadisticas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        EstadisticasVentasDTO estadisticas =
                prediccionService.obtenerEstadisticas(inicio, fin);

        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Obtiene estadísticas del último mes
     * GET /api/predicciones/estadisticas/ultimo-mes
     */
    @GetMapping("/estadisticas/ultimo-mes")
    public ResponseEntity<EstadisticasVentasDTO> estadisticasUltimoMes() {
        LocalDate fin = LocalDate.now();
        LocalDate inicio = fin.minusDays(30);

        EstadisticasVentasDTO estadisticas =
                prediccionService.obtenerEstadisticas(inicio, fin);

        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Limpia predicciones antiguas
     * DELETE /api/predicciones/limpiar
     */
    @DeleteMapping("/limpiar")
    public ResponseEntity<String> limpiarPrediccionesAntiguas() {
        prediccionService.limpiarPrediccionesAntiguas();
        return ResponseEntity.ok("Predicciones antiguas eliminadas correctamente");
    }
}