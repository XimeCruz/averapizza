package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.ProductoMasVendido;
import com.xime.averapizza.dto.ReporteDiarioResponse;
import com.xime.averapizza.model.DetalleVenta;
import com.xime.averapizza.model.Venta;
import com.xime.averapizza.repository.DetalleVentaRepository;
import com.xime.averapizza.repository.VentaRepository;
import com.xime.averapizza.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    @Override
    public ReporteDiarioResponse reporteDiario() {

        LocalDate hoy = LocalDate.now();

        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(23,59,59);

        List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);

        double totalDia = ventas.stream()
                .mapToDouble(Venta::getTotal)
                .sum();

        int cantidadPedidos = ventas.size();

        // Productos más vendidos (map)
        Map<String, Integer> acumulado = new HashMap<>();

        for (Venta v : ventas) {
            for (DetalleVenta dv : v.getDetalles()) {
                acumulado.merge(
                        dv.getProducto().getNombre(),
                        dv.getCantidad(),
                        Integer::sum
                );
            }
        }

        List<ProductoMasVendido> productos = acumulado.entrySet().stream()
                .map(e -> ProductoMasVendido.builder()
                        .nombre(e.getKey())
                        .cantidad(e.getValue())
                        .build())
                .toList();

        return ReporteDiarioResponse.builder()
                .totalVendido(totalDia)
                .totalPedidos(cantidadPedidos)
                .productos(productos)
                .build();
    }
}
