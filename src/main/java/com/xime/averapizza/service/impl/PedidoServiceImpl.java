package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.*;
import com.xime.averapizza.model.*;
import com.xime.averapizza.repository.*;
import com.xime.averapizza.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detalleRepository;
    private final ProductoRepository productoRepository;

    // NUEVO
    private final PresentacionProductoRepository presentacionRepository;
    private final SaborPizzaRepository saborPizzaRepository;

    private final PizzaPricingService pizzaPricingService;
    private final InventarioService inventarioService;
    private final VentaService ventaService;
    private final UsuarioRepository usuarioRepository;

    private final RecetaService recetaService;
    private final InsumoService insumoService;

    @Override
    @Transactional
    public PedidoResponseDTO crearPedido(CrearPedidoRequest request) {

        // ================================
        // CREAR PEDIDO BASE
        // ================================
        Pedido pedido = new Pedido();
        pedido.setFechaHora(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setTipoServicio(TipoServicio.valueOf(request.getTipoServicio()));
        pedido.setUsuarioId(request.getUsuarioId());
        pedido = pedidoRepository.save(pedido);

        double total = 0.0;
        List<DetallePedidoItem> items = new ArrayList<>();


        // ============================================================
        // 1) PROCESAR PRODUCTOS NORMALES (BEBIDAS, POSTRES, ETC.)
        // ============================================================
        if (request.getDetalles() != null && !request.getDetalles().isEmpty()) {

            for (DetallePedidoRequestDTO detReq : request.getDetalles()) {

                Producto producto = productoRepository.findById(detReq.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                PresentacionProducto presentacion = presentacionRepository.findById(detReq.getPresentacionId())
                        .orElseThrow(() -> new RuntimeException("Presentación no encontrada"));

                // precio de la BD (producto + presentación)
                Double precioUnit = presentacion.getPrecioBase();
                Double subtotal = precioUnit * detReq.getCantidad();
                total += subtotal;

                // Guardar detalle
                DetallePedido dp = new DetallePedido();
                dp.setPedido(pedido);
                dp.setProducto(producto);
                dp.setPresentacion(presentacion);
                dp.setCantidad(detReq.getCantidad());
                dp.setPrecioUnitario(precioUnit);
                dp.setSubtotal(subtotal);

                detalleRepository.save(dp);

                items.add(
                        DetallePedidoItem.builder()
                                .productoNombre(producto.getNombre())
                                .cantidad(detReq.getCantidad())
                                .subtotal(subtotal)
                                .build()
                );
            }
        }



        // ============================================================
        // 2) PROCESAR PIZZAS (PESO / REDONDA / BANDEJA)
        // ============================================================
        if (request.getPizzas() != null && !request.getPizzas().isEmpty()) {

            for (DetallePedidoPizzaDTO pzReq : request.getPizzas()) {

                PresentacionProducto presentacion = presentacionRepository.findById(pzReq.getPresentacionId())
                        .orElseThrow(() -> new RuntimeException("Presentación no encontrada"));

                SaborPizza sabor1 = saborPizzaRepository.findById(pzReq.getSabor1Id())
                        .orElseThrow(() -> new RuntimeException("Sabor1 no encontrado"));

                SaborPizza sabor2 = (pzReq.getSabor2Id() != null && pzReq.getSabor2Id() != 0)
                        ? saborPizzaRepository.findById(pzReq.getSabor2Id()).orElse(null)
                        : null;

                SaborPizza sabor3 = (pzReq.getSabor3Id() != null && pzReq.getSabor3Id() != 0)
                        ? saborPizzaRepository.findById(pzReq.getSabor3Id()).orElse(null)
                        : null;

                // precio según la lógica de precios de la pizza
                Double precioUnit = pizzaPricingService.calcularPrecio(
                        presentacion,
                        sabor1,
                        sabor2,
                        sabor3,
                        pzReq.getPesoKg()
                );

                System.out.println(precioUnit);
                Double subtotal = precioUnit * pzReq.getCantidad();
                total += subtotal;
                System.out.println(total);

                // Descontar inventario por sabor
                inventarioService.descontarPorSabor(sabor1, pzReq.getCantidad());
                if (sabor2 != null) inventarioService.descontarPorSabor(sabor2, pzReq.getCantidad());
                if (sabor3 != null) inventarioService.descontarPorSabor(sabor3, pzReq.getCantidad());

                Producto producto = productoRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                // guardar detalle
                DetallePedido dp = new DetallePedido();
                dp.setPedido(pedido);
                dp.setPresentacion(presentacion);
                dp.setProducto(producto);
                dp.setSabor1(sabor1);
                dp.setSabor2(sabor2);
                dp.setSabor3(sabor3);
                dp.setPesoKg(pzReq.getPesoKg());
                dp.setCantidad(pzReq.getCantidad());
                dp.setPrecioUnitario(precioUnit);
                dp.setSubtotal(subtotal);

                detalleRepository.save(dp);

                items.add(
                        DetallePedidoItem.builder()
                                .productoNombre("Pizza Especial")
                                .cantidad(pzReq.getCantidad())
                                .subtotal(subtotal)
                                .build()
                );
            }
        }



        // ================================
        // TOTAL FINAL
        // ================================
        pedido.setTotal(total);
        pedidoRepository.save(pedido);

        return PedidoResponseDTO.builder()
                .pedidoId(pedido.getId())
                .total(total)
                .estado(pedido.getEstado().name())
                .tipoServicio(pedido.getTipoServicio().name())
                .items(items)
                .build();
    }


    // ==========================================================
    //                    CAMBIAR ESTADO
    // ==========================================================
    @Override
    public Pedido cambiarEstado(Long pedidoId, EstadoPedido nuevoEstado, Integer usuarioId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        EstadoPedido estadoAnterior = pedido.getEstado();

        if (nuevoEstado == EstadoPedido.LISTO && estadoAnterior != EstadoPedido.LISTO) {
            descontarInsumosDelPedido(pedido, usuarioId);
            pedido.setFechaListo(LocalDateTime.now());
        }

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    private void descontarInsumosDelPedido(Pedido pedido, Integer usuarioId) {
        // Recopilar TODOS los insumos necesarios del pedido
        List<InsumoCalculadoDTO> todosLosInsumos = new ArrayList<>();

        for (DetallePedido detalle : pedido.getDetalles()) {
            List<InsumoCalculadoDTO> insumosDetalle =
                    recetaService.calcularInsumosParaDetalle(detalle);
            todosLosInsumos.addAll(insumosDetalle);
        }

        // Agrupar insumos repetidos
        Map<Long, InsumoCalculadoDTO> insumosAgrupados = todosLosInsumos.stream()
                .collect(Collectors.toMap(
                        InsumoCalculadoDTO::getInsumoId,
                        dto -> dto,
                        (dto1, dto2) -> {
                            dto1.setCantidadNecesaria(
                                    dto1.getCantidadNecesaria() + dto2.getCantidadNecesaria()
                            );
                            return dto1;
                        }
                ));

        List<InsumoCalculadoDTO> insumosFinales = new ArrayList<>(insumosAgrupados.values());

        // Verificar stock ANTES de descontar
        insumoService.verificarStockDisponible(insumosFinales);

        // Descontar cada insumo
        String referencia = "PEDIDO #" + pedido.getId();
        for (InsumoCalculadoDTO insumo : insumosFinales) {
            insumoService.descontarStock(
                    insumo.getInsumoId(),
                    insumo.getCantidadNecesaria(),
                    referencia,
                    usuarioId
            );
        }
    }

    // ==========================================================
    //                    LISTAR POR ESTADO
    // ==========================================================
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorEstado(String estado) {
        List<Pedido> pedidos = pedidoRepository.findByEstado(EstadoPedido.valueOf(estado));
        return pedidos.stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ==========================================================
    //                    COCINA
    // ==========================================================
    @Override
    @Transactional
    public PedidoResponseDTO tomarParaPreparacion(Long pedidoId) {
        Pedido pedido = obtenerPedido(pedidoId);

        if (pedido.getEstado() != EstadoPedido.PENDIENTE)
            throw new RuntimeException("Solo pedidos PENDIENTE pueden ser tomados.");

        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        pedidoRepository.save(pedido);
        return mapToResponse(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO marcarListo(Long pedidoId) {
        Pedido pedido = obtenerPedido(pedidoId);

        if (pedido.getEstado() != EstadoPedido.EN_PREPARACION)
            throw new RuntimeException("Solo pedidos EN_PREPARACION pueden marcarse LISTO.");

        pedido.setEstado(EstadoPedido.LISTO);
        pedidoRepository.save(pedido);
        return mapToResponse(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO marcarEntregado(Long pedidoId) {
        Pedido pedido = obtenerPedido(pedidoId);

        if (pedido.getEstado() != EstadoPedido.LISTO)
            throw new RuntimeException("Solo pedidos LISTO pueden ser entregados.");

        pedido.setEstado(EstadoPedido.ENTREGADO);
        pedidoRepository.save(pedido);

        // Crear la venta
        ventaService.registrarVentaDesdePedido(pedido);

        return mapToResponse(pedido);
    }

    // ==========================================================
    //                 CANCELAR PEDIDO + DEVOLVER STOCK
    // ==========================================================
    @Override
    @Transactional
    public PedidoResponseDTO cancelar(Long pedidoId) {

        Pedido pedido = obtenerPedido(pedidoId);

        if (pedido.getEstado() == EstadoPedido.ENTREGADO)
            throw new RuntimeException("No puedes cancelar un pedido ya ENTREGADO.");

        // Devolver stock sabor por sabor
        for (DetallePedido det : pedido.getDetalles()) {

            if (det.getSabor1() != null) inventarioService.devolverPorSabor(det.getSabor1(), det.getCantidad());
            if (det.getSabor2() != null) inventarioService.devolverPorSabor(det.getSabor2(), det.getCantidad());
            if (det.getSabor3() != null) inventarioService.devolverPorSabor(det.getSabor3(), det.getCantidad());
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);

        return mapToResponse(pedido);
    }

    @Override
    public Pedido obtenerPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId).orElse(null);
    }

    // ==========================================================
    //                   Métodos privados
    // ==========================================================
    private Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
    }

    private PedidoResponseDTO mapToResponse(Pedido pedido) {
        List<DetallePedidoItem> items = new ArrayList<>();

        for (DetallePedido det : pedido.getDetalles()) {
            items.add(
                    DetallePedidoItem.builder()
                            .id(Math.toIntExact(det.getId()))
                            .pedidoId(Math.toIntExact(det.getPedido().getId()))
                            .productoNombre(det.getProducto().getNombre())
                            .presentacion(det.getPresentacion().getTipo().name())
                            .sabor1(det.getSabor1() != null ? det.getSabor1().getNombre() : "")
                            .sabor2(det.getSabor2() != null ? det.getSabor2().getNombre() : "")
                            .sabor3(det.getSabor3() != null ? det.getSabor3().getNombre() : "")
                            .cantidad(det.getCantidad())
                            .subtotal(det.getSubtotal())
                            .build()
            );
        }
        Usuario usuario = usuarioRepository.findById(Long.valueOf(pedido.getUsuarioId()))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + pedido.getUsuarioId()));

        return PedidoResponseDTO.builder()
                .pedidoId(pedido.getId())
                .total(pedido.getTotal())
                .estado(pedido.getEstado().name())
                .tipoServicio(pedido.getTipoServicio().name())
                .fechaHora(pedido.getFechaHora())
                .idUsuario(pedido.getUsuarioId())
                .nombreUsuario(usuario.getNombre())
                .items(items)
                .build();
    }

    @Override
    public PedidoResponseDTO obtenerPedidoDTO(Long pedidoId) {
        Pedido pedido = obtenerPedido(pedidoId);
        return mapToResponse(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPedidosHoy() {

        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = inicio.plusHours(23).plusMinutes(59).plusSeconds(59);

        List<Pedido> lista = pedidoRepository.findByFechaHoraBetween(inicio, fin);


        return lista.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorRango(String inicio, String fin) {

        LocalDateTime ini = LocalDateTime.parse(inicio);
        LocalDateTime fi = LocalDateTime.parse(fin);

        List<Pedido> lista = pedidoRepository.findByFechaHoraBetween(ini, fi);

        return lista.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerHistorialPedidos(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioIdOrderByFechaHoraDesc(clienteId.intValue());

        return pedidos.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerPedidosDelDia(Long clienteId) {
        LocalDateTime inicioDia = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime finDia = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

        List<Pedido> pedidos = pedidoRepository.findByUsuarioIdAndFechaHoraBetweenOrderByFechaHoraDesc(
                clienteId.intValue(),
                inicioDia,
                finDia
        );

        return pedidos.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }



}
