package com.xime.averapizza.dto;


import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaborVentasDTO {

    private Long saborId;
    private String saborNombre;
    private Double totalVendido;  // suma de todas las presentaciones
    private Integer cantidadPedidos;
    private List<VentaPorPresentacion> detallesPorPresentacion;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VentaPorPresentacion {
        private Long presentacionId;
        private String tipoPresentacion;  // PESO, REDONDA, BANDEJA
        private Double cantidadVendida;   // kg para PESO, unidades para REDONDA/BANDEJA
        private Integer numeroVentas;     // cantidad de veces que se vendió
        private Double ingresoTotal;      // total de dinero generado
    }
}
