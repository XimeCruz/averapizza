package com.xime.averapizza.service;

import com.xime.averapizza.dto.InsumoBajoStockDTO;
import com.xime.averapizza.dto.ProductoTopDTO;
import com.xime.averapizza.dto.ReporteDiarioResponse;
import com.xime.averapizza.dto.VentaResumenDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReporteService {

    ReporteDiarioResponse reporteDiario();

    List<VentaResumenDTO> ventasHoy();

    List<VentaResumenDTO> ventasEntreFechas(LocalDateTime inicio, LocalDateTime fin);

    List<ProductoTopDTO> productosTop();

    List<InsumoBajoStockDTO> inventarioBajoStock();

}
