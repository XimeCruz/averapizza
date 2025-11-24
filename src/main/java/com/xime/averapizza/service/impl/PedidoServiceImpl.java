package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.*;
import com.xime.averapizza.model.*;
import com.xime.averapizza.repository.*;
import com.xime.averapizza.service.InventarioService;
import com.xime.averapizza.service.PedidoService;
import com.xime.averapizza.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detalleRepository;
    private final ProductoRepository productoRepository;
    private final InventarioService inventarioService;
    private final VentaService ventaService;

    @Override
    @Transactional
    public PedidoResponseDTO crearPedido(CrearPedidoRequest request) {

        Pedido pedido = new Pedido();
        pedido.setFechaHora(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setTipoServicio(TipoServicio.valueOf(request.getTipoServicio()));
        pedido.setUsuarioId(request.getUsuarioId());

        pedido = pedidoRepository.save(pedido);

        double total = 0.0;
        List<DetallePedidoItem> items = new ArrayList<>();

        for (DetallePedidoRequestDTO detReq : request.getDetalles()) {

            Producto producto = productoRepository.findById(detReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            double subtotal = producto.getPrecio() * detReq.getCantidad();
            total += subtotal;

            // Descontar stock si tiene receta:
            inventarioService.descontarStockPorProducto(
                    producto,
                    detReq.getCantidad(),
                    request.getUsuarioId(),
                    "VENTA_PEDIDO_" + pedido.getId()
            );

            // Guardar detalle
            DetallePedido dp = new DetallePedido();
            dp.setPedido(pedido);
            dp.setProducto(producto);
            dp.setCantidad(detReq.getCantidad());
            dp.setPrecioUnitario(producto.getPrecio());
            dp.setSubtotal(subtotal);
            detalleRepository.save(dp);

            items.add(
                    DetallePedidoItem.builder()
                            .producto(producto.getNombre())
                            .cantidad(detReq.getCantidad())
                            .subtotal(subtotal)
                            .build()
            );
        }

        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);

        return PedidoResponseDTO.builder()
                .pedidoId(pedido.getId())
                .total(total)
                .estado(pedido.getEstado().name())
                .tipoServicio(pedido.getTipoServicio().name())
                .items(items)
                .build();
    }

    @Override
    @Transactional
    public PedidoResponseDTO cambiarEstado(Long pedidoId, String nuevoEstado) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(EstadoPedido.valueOf(nuevoEstado));
        pedidoRepository.save(pedido);

        return PedidoResponseDTO.builder()
                .pedidoId(pedido.getId())
                .total(pedido.getTotal())
                .estado(pedido.getEstado().name())
                .tipoServicio(pedido.getTipoServicio().name())
                .build();
    }

    @Override
    public List<PedidoResponseDTO> listarPorEstado(String estado) {
        List<Pedido> pedidos;

        if (estado == null || estado.isBlank()) {
            pedidos = pedidoRepository.findAll();
        } else {
            EstadoPedido est = EstadoPedido.valueOf(estado.toUpperCase());
            pedidos = pedidoRepository.findByEstado(est);
        }

        List<PedidoResponseDTO> lista = new ArrayList<>();
        for (Pedido p : pedidos) {
            lista.add(mapToResponse(p));
        }
        return lista;
    }

    @Override
    @Transactional
    public PedidoResponseDTO tomarParaPreparacion(Long pedidoId) {
        Pedido pedido = obtenerPedido(pedidoId);

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new RuntimeException("Solo se pueden tomar pedidos en estado PENDIENTE.");
        }

        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        pedidoRepository.save(pedido);

        return mapToResponse(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO marcarListo(Long pedidoId) {
        Pedido pedido = obtenerPedido(pedidoId);

        if (pedido.getEstado() != EstadoPedido.EN_PREPARACION) {
            throw new RuntimeException("Solo se pueden marcar como LISTO pedidos EN_PREPARACION.");
        }

        pedido.setEstado(EstadoPedido.LISTO);
        pedidoRepository.save(pedido);

        return mapToResponse(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO marcarEntregado(Long pedidoId) {

        Pedido pedido = obtenerPedido(pedidoId);

        if (pedido.getEstado() != EstadoPedido.LISTO) {
            throw new RuntimeException("Solo se pueden marcar como ENTREGADO pedidos LISTO.");
        }

        pedido.setEstado(EstadoPedido.ENTREGADO);
        pedidoRepository.save(pedido);

        // 🔥 Crear la venta
        ventaService.registrarVentaDesdePedido(pedido);

        return mapToResponse(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO cancelar(Long pedidoId) {

        Pedido pedido = obtenerPedido(pedidoId);

        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            throw new RuntimeException("No se puede cancelar un pedido YA ENTREGADO.");
        }

        // 🔥 Revertir stock si el pedido fue descontado previamente
        for (DetallePedido det : pedido.getDetalles()) {

            Producto producto = det.getProducto();
            Integer cantidad = det.getCantidad();

            inventarioService.devolverStockPorProducto(
                    producto,
                    cantidad,
                    pedido.getUsuarioId(),
                    "CANCELACION_PEDIDO_" + pedido.getId()
            );
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);

        return mapToResponse(pedido);
    }

    // =================== helpers privados ===================

    private Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
    }

    private PedidoResponseDTO mapToResponse(Pedido pedido) {
        List<DetallePedidoItem> items = new ArrayList<>();

        if (pedido.getDetalles() != null) {
            for (DetallePedido det : pedido.getDetalles()) {
                items.add(DetallePedidoItem.builder()
                        .producto(det.getProducto().getNombre())
                        .cantidad(det.getCantidad())
                        .subtotal(det.getSubtotal())
                        .build());
            }
        }

        return PedidoResponseDTO.builder()
                .pedidoId(pedido.getId())
                .total(pedido.getTotal())
                .estado(pedido.getEstado().name())
                .tipoServicio(pedido.getTipoServicio().name())
                .items(items)
                .build();
    }
}
