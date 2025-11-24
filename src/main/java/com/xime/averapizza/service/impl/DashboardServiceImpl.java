package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.DashboardResponse;
import com.xime.averapizza.model.Venta;
import com.xime.averapizza.repository.VentaRepository;
import com.xime.averapizza.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final VentaRepository ventaRepository;

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
