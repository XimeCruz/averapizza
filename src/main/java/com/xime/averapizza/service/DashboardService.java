package com.xime.averapizza.service;

import com.xime.averapizza.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface DashboardService {

    DashboardResponse getDashboard();

    MetricasGeneralesDTO obtenerMetricasGenerales();

    Long contarTotalPedidos();

    Double calcularTasaEntrega();

    Long contarTotalProductos();

    // ============ ALERTAS Y STOCK ============

    List<AlertaStockDTO> obtenerAlertasStock();

    // ============ VENTAS ============

    VentasDiaDTO obtenerVentasDelDia();

    VentasDiaDTO obtenerVentasPorRango(LocalDate fechaInicio, LocalDate fechaFin);

    // ============ ESTADO DE PEDIDOS ============

    Integer contarPedidosPendientes();

    Integer contarPedidosEnCocina();

    Integer contarPedidosEntregados();

    EstadosPedidosDTO obtenerEstadosPedidos();

    // ============ CLIENTES - INFORMACIÓN GENERAL ============

    Long contarTotalClientes();

    Long contarClientesActivos();

    Long contarClientesInactivos();

    EstadisticasClientesDTO obtenerEstadisticasClientes();

    // ============ CLIENTES - LISTAS ============

    List<ClienteInfoDTO> obtenerTodosLosClientes();

    List<ClienteInfoDTO> obtenerClientesActivos();

    List<ClienteInfoDTO> obtenerClientesInactivos();

    // ============ CLIENTE ESPECÍFICO - MÉTRICAS ============

    Long contarPedidosPorCliente(Long clienteId);

    Double calcularTotalGastado(Long clienteId);

    PizzaFavoritaDTO obtenerPizzaFavorita(Long clienteId);

    List<HistorialPedidoDTO> obtenerHistorialPedidos(Long clienteId);

    EstadisticasClienteDTO obtenerEstadisticasCliente(Long clienteId);

    // ============ CLIENTE - GESTIÓN DE PERFIL ============

    ClienteInfoDTO actualizarInformacionCliente(Long clienteId, ActualizarClienteDTO datos);

    void cambiarPassword(Long clienteId, CambiarPasswordDTO datos);

}
