package com.xime.averapizza.controller;

import com.xime.averapizza.dto.*;
import com.xime.averapizza.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "KPIS y metricas")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboard() {
        return dashboardService.getDashboard();
    }

    @GetMapping("/metricas-generales")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    public ResponseEntity<MetricasGeneralesDTO> obtenerMetricasGenerales() {
        return ResponseEntity.ok(dashboardService.obtenerMetricasGenerales());
    }

    @GetMapping("/total-pedidos")
    public ResponseEntity<Long> obtenerTotalPedidos() {
        return ResponseEntity.ok(dashboardService.contarTotalPedidos());
    }

    @GetMapping("/tasa-entrega")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    public ResponseEntity<Double> obtenerTasaEntrega() {
        return ResponseEntity.ok(dashboardService.calcularTasaEntrega());
    }

    @GetMapping("/total-productos")
    public ResponseEntity<Long> obtenerTotalProductos() {
        return ResponseEntity.ok(dashboardService.contarTotalProductos());
    }

    // ============ ALERTAS Y STOCK ============

    @GetMapping("/alertas-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'COCINA')")
    public ResponseEntity<List<AlertaStockDTO>> obtenerAlertasStock() {
        return ResponseEntity.ok(dashboardService.obtenerAlertasStock());
    }

    // ============ VENTAS ============

    @GetMapping("/ventas-dia")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    public ResponseEntity<VentasDiaDTO> obtenerVentasDelDia() {
        return ResponseEntity.ok(dashboardService.obtenerVentasDelDia());
    }

    @GetMapping("/ventas-rango")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    public ResponseEntity<VentasDiaDTO> obtenerVentasPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(dashboardService.obtenerVentasPorRango(fechaInicio, fechaFin));
    }

    // ============ ESTADO DE PEDIDOS ============

    @GetMapping("/pedidos/pendientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO', 'COCINA')")
    public ResponseEntity<Integer> obtenerPedidosPendientes() {
        return ResponseEntity.ok(dashboardService.contarPedidosPendientes());
    }

    @GetMapping("/pedidos/en-cocina")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO', 'COCINA')")
    public ResponseEntity<Integer> obtenerPedidosEnCocina() {
        return ResponseEntity.ok(dashboardService.contarPedidosEnCocina());
    }

    @GetMapping("/pedidos/entregados")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    public ResponseEntity<Integer> obtenerPedidosEntregados() {
        return ResponseEntity.ok(dashboardService.contarPedidosEntregados());
    }

    @GetMapping("/pedidos/estados")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAJERO', 'COCINA')")
    public ResponseEntity<EstadosPedidosDTO> obtenerEstadosPedidos() {
        return ResponseEntity.ok(dashboardService.obtenerEstadosPedidos());
    }

    // ============ CLIENTES - INFORMACIÓN GENERAL ============

    @GetMapping("/clientes/total")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> obtenerTotalClientes() {
        return ResponseEntity.ok(dashboardService.contarTotalClientes());
    }

    @GetMapping("/clientes/activos/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> obtenerClientesActivos() {
        return ResponseEntity.ok(dashboardService.contarClientesActivos());
    }

    @GetMapping("/clientes/inactivos/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> obtenerClientesInactivos() {
        return ResponseEntity.ok(dashboardService.contarClientesInactivos());
    }

    @GetMapping("/clientes/estadisticas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstadisticasClientesDTO> obtenerEstadisticasClientes() {
        return ResponseEntity.ok(dashboardService.obtenerEstadisticasClientes());
    }

    // ============ CLIENTES - LISTAS ============

    @GetMapping("/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteInfoDTO>> obtenerTodosLosClientes() {
        return ResponseEntity.ok(dashboardService.obtenerTodosLosClientes());
    }

    @GetMapping("/clientes/activos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteInfoDTO>> obtenerClientesActivosList() {
        return ResponseEntity.ok(dashboardService.obtenerClientesActivos());
    }

    @GetMapping("/clientes/inactivos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteInfoDTO>> obtenerClientesInactivosList() {
        return ResponseEntity.ok(dashboardService.obtenerClientesInactivos());
    }

    // ============ CLIENTE ESPECÍFICO - MÉTRICAS ============

    @GetMapping("/clientes/{clienteId}/pedidos/total")
    public ResponseEntity<Long> obtenerTotalPedidosPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(dashboardService.contarPedidosPorCliente(clienteId));
    }

    @GetMapping("/clientes/{clienteId}/total-gastado")
    public ResponseEntity<Double> obtenerTotalGastadoPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(dashboardService.calcularTotalGastado(clienteId));
    }

    @GetMapping("/clientes/{clienteId}/pizza-favorita")
    public ResponseEntity<PizzaFavoritaDTO> obtenerPizzaFavorita(@PathVariable Long clienteId) {
        return ResponseEntity.ok(dashboardService.obtenerPizzaFavorita(clienteId));
    }

    @GetMapping("/clientes/{clienteId}/estadisticas")
    public ResponseEntity<EstadisticasClienteDTO> obtenerEstadisticasCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(dashboardService.obtenerEstadisticasCliente(clienteId));
    }

    // ============ CLIENTE - GESTIÓN DE PERFIL ============

    @PutMapping("/clientes/{clienteId}/informacion")
    public ResponseEntity<ClienteInfoDTO> editarInformacionPersonal(
            @PathVariable Long clienteId,
            @RequestBody ActualizarClienteDTO datos) {
        return ResponseEntity.ok(dashboardService.actualizarInformacionCliente(clienteId, datos));
    }

    @PutMapping("/clientes/{clienteId}/cambiar-password")
    public ResponseEntity<Void> cambiarPassword(
            @PathVariable Long clienteId,
            @RequestBody CambiarPasswordDTO datos) {
        dashboardService.cambiarPassword(clienteId, datos);
        return ResponseEntity.ok().build();
    }


}

