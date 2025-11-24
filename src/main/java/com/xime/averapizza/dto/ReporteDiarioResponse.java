package com.xime.averapizza.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ReporteDiarioResponse {

    private Double totalVendido;
    private Integer totalPedidos;
    private List<ProductoMasVendido> productos;
}
