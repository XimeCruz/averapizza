package com.xime.averapizza.service.impl;

import com.xime.averapizza.model.*;
import com.xime.averapizza.repository.DetalleVentaRepository;
import com.xime.averapizza.repository.VentaRepository;
import com.xime.averapizza.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    @Override
    @Transactional
    public void registrarVentaDesdePedido(Pedido pedido) {

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setPedido(pedido);
        venta.setTotal(pedido.getTotal());

        venta = ventaRepository.save(venta);

        for (DetallePedido dp : pedido.getDetalles()) {

            DetalleVenta dv = new DetalleVenta();
            dv.setVenta(venta);
            dv.setProducto(dp.getProducto());
            dv.setCantidad(dp.getCantidad());
            dv.setSubtotal(dp.getSubtotal());

            detalleVentaRepository.save(dv);
        }
    }
}
