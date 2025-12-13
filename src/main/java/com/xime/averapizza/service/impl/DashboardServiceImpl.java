package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.*;
import com.xime.averapizza.model.*;
import com.xime.averapizza.repository.*;
import com.xime.averapizza.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final VentaRepository ventaRepository;

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final InsumoRepository insumoRepository;
    //private final VentaRepository ventaRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final SaborPizzaRepository saborPizzaRepository;
    private final PasswordEncoder passwordEncoder;

    // ============ MÉTRICAS GENERALES ============

    @Transactional(readOnly = true)
    public MetricasGeneralesDTO obtenerMetricasGenerales() {
        return MetricasGeneralesDTO.builder()
                .totalPedidos(contarTotalPedidos())
                .tasaEntrega(calcularTasaEntrega())
                .totalProductos(contarTotalProductosConSabores())
                .alertasStock(contarAlertasStock())
                .metricasDia(obtenerMetricasDia())
                .build();
    }

    @Transactional(readOnly = true)
    public MetricasDiaDTO obtenerMetricasDia() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioDia = LocalDateTime.of(hoy, LocalTime.MIN);
        LocalDateTime finDia = LocalDateTime.of(hoy, LocalTime.MAX);

        // Ventas del día
        List<Venta> ventasDelDia = ventaRepository.findByFechaBetween(inicioDia, finDia);
        Double totalVentas = ventasDelDia.stream()
                .mapToDouble(Venta::getTotal)
                .sum();

        // Pedidos del día por estado
        Integer pedidosPendientes = pedidoRepository.countByEstadoAndFechaHoraBetween(
                EstadoPedido.PENDIENTE, inicioDia, finDia);

        Integer pedidosEnPreparacion = pedidoRepository.countByEstadoAndFechaHoraBetween(
                EstadoPedido.EN_PREPARACION, inicioDia, finDia);

        Integer pedidosEntregados = pedidoRepository.countByEstadoAndFechaHoraBetween(
                EstadoPedido.ENTREGADO, inicioDia, finDia);

        return MetricasDiaDTO.builder()
                .fecha(hoy)
                .totalVentas(Math.round(totalVentas * 100.0) / 100.0)
                .numeroVentas(ventasDelDia.size())
                .pedidosPendientes(pedidosPendientes)
                .pedidosEnPreparacion(pedidosEnPreparacion)
                .pedidosEntregados(pedidosEntregados)
                .build();
    }

    @Transactional(readOnly = true)
    public Integer contarAlertasStock() {
        return Math.toIntExact(
                insumoRepository.countByStockActualLessThanEqualStockMinimoAndActivoTrue()
        );
    }

    @Transactional(readOnly = true)
    public Long contarTotalProductosConSabores() {
        Long productosBase = productoRepository.countByActivoTrue();
        Long saboresActivos = saborPizzaRepository.countByActivoTrue();
        return productosBase + saboresActivos;
    }

    @Transactional(readOnly = true)
    public Long contarTotalPedidos() {
        return pedidoRepository.count();
    }

    @Transactional(readOnly = true)
    public Double calcularTasaEntrega() {
        long total = pedidoRepository.count();
        if (total == 0) return 0.0;

        long entregados = pedidoRepository.countByEstado(EstadoPedido.ENTREGADO);
        return (entregados * 100.0) / total;
    }

    @Transactional(readOnly = true)
    public Long contarTotalProductos() {
        return productoRepository.countByActivoTrue();
    }

    // ============ ALERTAS Y STOCK ============

    @Transactional(readOnly = true)
    public List<AlertaStockDTO> obtenerAlertasStock() {
        List<Insumo> insumosBajos = insumoRepository.findByStockActualLessThanEqualStockMinimo();

        return insumosBajos.stream()
                .map(insumo -> AlertaStockDTO.builder()
                        .insumoId(insumo.getId())
                        .nombre(insumo.getNombre())
                        .stockActual(insumo.getStockActual())
                        .stockMinimo(insumo.getStockMinimo())
                        .unidadMedida(insumo.getUnidadMedida())
                        .criticidad(calcularCriticidad(insumo))
                        .build())
                .collect(Collectors.toList());
    }

    private String calcularCriticidad(Insumo insumo) {
        double porcentaje = (insumo.getStockActual() / insumo.getStockMinimo()) * 100;
        if (porcentaje <= 25) return "CRÍTICO";
        if (porcentaje <= 50) return "BAJO";
        return "MEDIO";
    }

    // ============ VENTAS ============

    @Transactional(readOnly = true)
    public VentasDiaDTO obtenerVentasDelDia() {
        LocalDateTime inicioDelDia = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime finDelDia = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<Venta> ventas = ventaRepository.findByFechaBetween(inicioDelDia, finDelDia);

        Double totalVentas = ventas.stream()
                .mapToDouble(Venta::getTotal)
                .sum();

        return VentasDiaDTO.builder()
                .fecha(LocalDate.now())
                .totalVentas(totalVentas)
                .numeroVentas(ventas.size())
                .promedioVenta(ventas.isEmpty() ? 0.0 : totalVentas / ventas.size())
                .build();
    }

    @Transactional(readOnly = true)
    public VentasDiaDTO obtenerVentasPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = LocalDateTime.of(fechaInicio, LocalTime.MIN);
        LocalDateTime fin = LocalDateTime.of(fechaFin, LocalTime.MAX);

        List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);

        Double totalVentas = ventas.stream()
                .mapToDouble(Venta::getTotal)
                .sum();

        return VentasDiaDTO.builder()
                .fecha(fechaInicio)
                .totalVentas(totalVentas)
                .numeroVentas(ventas.size())
                .promedioVenta(ventas.isEmpty() ? 0.0 : totalVentas / ventas.size())
                .build();
    }

    // ============ ESTADO DE PEDIDOS ============

    @Transactional(readOnly = true)
    public Integer contarPedidosPendientes() {
        return Math.toIntExact(pedidoRepository.countByEstado(EstadoPedido.PENDIENTE));
    }

    @Transactional(readOnly = true)
    public Integer contarPedidosEnCocina() {
        return Math.toIntExact(pedidoRepository.countByEstado(EstadoPedido.EN_PREPARACION));
    }

    @Transactional(readOnly = true)
    public Integer contarPedidosEntregados() {
        return Math.toIntExact(pedidoRepository.countByEstado(EstadoPedido.ENTREGADO));
    }

    @Transactional(readOnly = true)
    public EstadosPedidosDTO obtenerEstadosPedidos() {
        return EstadosPedidosDTO.builder()
                .pendientes(contarPedidosPendientes())
                .enPreparacion(contarPedidosEnCocina())
                .listos(Math.toIntExact(pedidoRepository.countByEstado(EstadoPedido.LISTO)))
                .entregados(contarPedidosEntregados())
                .cancelados(Math.toIntExact(pedidoRepository.countByEstado(EstadoPedido.CANCELADO)))
                .build();
    }

    // ============ CLIENTES - INFORMACIÓN GENERAL ============

    @Transactional(readOnly = true)
    public Long contarTotalClientes() {
        return usuarioRepository.countByRolesNombre(Rol.RolNombre.CLIENTE);
    }

    @Transactional(readOnly = true)
    public Long contarClientesActivos() {
        return usuarioRepository.countByActivoTrueAndRolesNombre(Rol.RolNombre.CLIENTE);
    }

    @Transactional(readOnly = true)
    public Long contarClientesInactivos() {
        return usuarioRepository.countByActivoFalseAndRolesNombre(Rol.RolNombre.CLIENTE);
    }

    @Transactional(readOnly = true)
    public EstadisticasClientesDTO obtenerEstadisticasClientes() {
        return EstadisticasClientesDTO.builder()
                .totalClientes(contarTotalClientes())
                .clientesActivos(contarClientesActivos())
                .clientesInactivos(contarClientesInactivos())
                .build();
    }

    // ============ CLIENTES - LISTAS ============

    @Transactional(readOnly = true)
    public List<ClienteInfoDTO> obtenerTodosLosClientes() {
        List<Usuario> clientes = usuarioRepository.findByRolesNombre(Rol.RolNombre.CLIENTE);
        return mapearAClienteInfoDTO(clientes);
    }

    @Transactional(readOnly = true)
    public List<ClienteInfoDTO> obtenerClientesActivos() {
        List<Usuario> clientes = usuarioRepository.findByActivoTrueAndRolesNombre(Rol.RolNombre.CLIENTE);
        return mapearAClienteInfoDTO(clientes);
    }

    @Transactional(readOnly = true)
    public List<ClienteInfoDTO> obtenerClientesInactivos() {
        List<Usuario> clientes = usuarioRepository.findByActivoFalseAndRolesNombre(Rol.RolNombre.CLIENTE);
        return mapearAClienteInfoDTO(clientes);
    }

    private List<ClienteInfoDTO> mapearAClienteInfoDTO(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(usuario -> ClienteInfoDTO.builder()
                        .id(usuario.getId())
                        .nombre(usuario.getNombre())
                        .correo(usuario.getCorreo())
                        .activo(usuario.isActivo())
                        .totalPedidos(contarPedidosPorCliente(usuario.getId()))
                        .totalGastado(calcularTotalGastado(usuario.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    // ============ CLIENTE ESPECÍFICO - MÉTRICAS ============

    @Transactional(readOnly = true)
    public Long contarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.countByUsuarioId(clienteId.intValue());
    }

    @Transactional(readOnly = true)
    public Double calcularTotalGastado(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioIdAndEstadoNot(
                clienteId.intValue(),
                EstadoPedido.CANCELADO
        );

        return pedidos.stream()
                .mapToDouble(Pedido::getTotal)
                .sum();
    }

    @Transactional(readOnly = true)
    public PizzaFavoritaDTO obtenerPizzaFavorita(Long clienteId) {
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoUsuarioIdAndProductoTipoProducto(
                clienteId.intValue(),
                Producto.TipoProducto.PIZZA
        );

        // Agrupar por sabor y contar
        var saborConteo = detalles.stream()
                .flatMap(det -> {
                    var sabores = new java.util.ArrayList<SaborPizza>();
                    if (det.getSabor1() != null) sabores.add(det.getSabor1());
                    if (det.getSabor2() != null) sabores.add(det.getSabor2());
                    if (det.getSabor3() != null) sabores.add(det.getSabor3());
                    return sabores.stream();
                })
                .collect(Collectors.groupingBy(
                        SaborPizza::getNombre,
                        Collectors.counting()
                ));

        if (saborConteo.isEmpty()) {
            return PizzaFavoritaDTO.builder()
                    .nombreSabor("Sin pizzas ordenadas")
                    .vecesOrdenada(0L)
                    .build();
        }

        var saborFavorito = saborConteo.entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .get();

        return PizzaFavoritaDTO.builder()
                .nombreSabor(saborFavorito.getKey())
                .vecesOrdenada(saborFavorito.getValue())
                .build();
    }

    @Transactional(readOnly = true)
    public List<HistorialPedidoDTO> obtenerHistorialPedidos(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioIdOrderByFechaHoraDesc(clienteId.intValue());

        return pedidos.stream()
                .map(pedido -> HistorialPedidoDTO.builder()
                        .pedidoId(pedido.getId())
                        .fecha(pedido.getFechaHora())
                        .total(pedido.getTotal())
                        .estado(pedido.getEstado().name())
                        .tipoServicio(pedido.getTipoServicio().name())
                        .numeroProductos(pedido.getDetalles() != null ? pedido.getDetalles().size() : 0)
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstadisticasClienteDTO obtenerEstadisticasCliente(Long clienteId) {
        Long totalPedidos = contarPedidosPorCliente(clienteId);
        Double totalGastado = calcularTotalGastado(clienteId);
        PizzaFavoritaDTO pizzaFavorita = obtenerPizzaFavorita(clienteId);

        return EstadisticasClienteDTO.builder()
                .totalPedidos(totalPedidos)
                .totalGastado(totalGastado)
                .promedioGasto(totalPedidos > 0 ? totalGastado / totalPedidos : 0.0)
                .pizzaFavorita(pizzaFavorita.getNombreSabor())
                .build();
    }

    // ============ CLIENTE - GESTIÓN DE PERFIL ============

    @Transactional
    public ClienteInfoDTO actualizarInformacionCliente(Long clienteId, ActualizarClienteDTO datos) {
        Usuario usuario = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (datos.getNombre() != null && !datos.getNombre().isBlank()) {
            usuario.setNombre(datos.getNombre());
        }

        if (datos.getCorreo() != null && !datos.getCorreo().isBlank()) {
            // Validar que el correo no esté en uso por otro usuario
            if (usuarioRepository.existsByCorreoAndIdNot(datos.getCorreo(), clienteId)) {
                throw new RuntimeException("El correo ya está en uso");
            }
            usuario.setCorreo(datos.getCorreo());
        }

        usuario = usuarioRepository.save(usuario);

        return ClienteInfoDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .activo(usuario.isActivo())
                .totalPedidos(contarPedidosPorCliente(usuario.getId()))
                .totalGastado(calcularTotalGastado(usuario.getId()))
                .build();
    }

    @Transactional
    public void cambiarPassword(Long clienteId, CambiarPasswordDTO datos) {
        Usuario usuario = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Verificar password actual
        if (!passwordEncoder.matches(datos.getPasswordActual(), usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // Validar que las nuevas contraseñas coincidan
        if (!datos.getPasswordNueva().equals(datos.getPasswordConfirmacion())) {
            throw new RuntimeException("Las contraseñas nuevas no coinciden");
        }

        // Actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(datos.getPasswordNueva()));
        usuarioRepository.save(usuario);
    }

    @Override
    public DashboardResponse getDashboard() {

        LocalDate hoy = LocalDate.now();

        // Día
        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = hoy.atTime(23, 59, 59);

        List<Venta> ventasDia = ventaRepository.findByFechaBetween(inicioDia, finDia);
        double totalDia = ventasDia.stream().mapToDouble(Venta::getTotal).sum();

        // Mes
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDateTime inicioMesDT = inicioMes.atStartOfDay();
        LocalDateTime finMesDT = hoy.atTime(23, 59, 59);

        List<Venta> ventasMes = ventaRepository.findByFechaBetween(inicioMesDT, finMesDT);
        double totalMes = ventasMes.stream().mapToDouble(Venta::getTotal).sum();

        double ticketProm = ventasMes.isEmpty()
                ? 0.0
                : totalMes / ventasMes.size();

        return DashboardResponse.builder()
                .ventasDelDia(totalDia)
                .pedidosDelDia(ventasDia.size())
                .ventasDelMes(totalMes)
                .pedidosDelMes(ventasMes.size())
                .ticketPromedio(ticketProm)
                .build();
    }


}
