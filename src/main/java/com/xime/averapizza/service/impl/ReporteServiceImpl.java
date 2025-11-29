package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.*;
import com.xime.averapizza.model.DetalleVenta;
import com.xime.averapizza.model.Venta;
import com.xime.averapizza.repository.DetalleVentaRepository;
import com.xime.averapizza.repository.InsumoRepository;
import com.xime.averapizza.repository.VentaRepository;
import com.xime.averapizza.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepo;
    private final DetalleVentaRepository detalleVentaRepository;
    private final InsumoRepository insumoRepo;


    @Override
    public ReporteDiarioResponse reporteDiario() {

        LocalDate hoy = LocalDate.now();

        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(23,59,59);

        List<Venta> ventas = ventaRepo.findByFechaBetween(inicio, fin);

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

    @Override
    public List<VentaResumenDTO> ventasHoy() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(LocalTime.MAX);

        return ventaRepo.ventasEntreFechas(inicio, fin)
                .stream()
                .map(v -> new VentaResumenDTO(
                        v.getId(),
                        v.getFecha(),
                        v.getTotal()
                ))
                .toList();
    }

    @Override
    public List<VentaResumenDTO> ventasEntreFechas(LocalDateTime inicio, LocalDateTime fin) {

        return ventaRepo.ventasEntreFechas(inicio, fin)
                .stream()
                .map(v -> new VentaResumenDTO(
                        v.getId(),
                        v.getFecha(),
                        v.getTotal()
                ))
                .toList();
    }

    @Override
    public List<ProductoTopDTO> productosTop() {
        return ventaRepo.productosMasVendidos()
                .stream()
                .map(obj -> new ProductoTopDTO(
                        (String) obj[0],
                        (Long) obj[1]
                ))
                .toList();
    }

    @Override
    public List<InsumoBajoStockDTO> inventarioBajoStock() {
        return insumoRepo.findInsumosBajoStock()
                .stream()
                .map(i -> new InsumoBajoStockDTO(
                        i.getId(),
                        i.getNombre(),
                        i.getStockActual(),
                        i.getStockMinimo()
                ))
                .toList();
    }
}
