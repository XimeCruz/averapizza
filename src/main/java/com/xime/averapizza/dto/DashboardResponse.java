package com.xime.averapizza.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

    private Double ventasDelDia;
    private Integer pedidosDelDia;
    private Double ventasDelMes;
    private Integer pedidosDelMes;
    private Double ticketPromedio;
}
