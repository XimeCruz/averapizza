package com.xime.averapizza.dto;

import com.xime.averapizza.model.TipoServicio;

import java.time.LocalDateTime;

public class VentasPorTipoDTO {
    private TipoServicio tipoServicio;
    private Long cantidadVentas;
    private Double total;

    public VentasPorTipoDTO(TipoServicio tipoServicio, Long cantidadVentas, Double total) {
        this.tipoServicio = tipoServicio;
        this.cantidadVentas = cantidadVentas;
        this.total = total;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Long getCantidadVentas() {
        return cantidadVentas;
    }

    public void setCantidadVentas(Long cantidadVentas) {
        this.cantidadVentas = cantidadVentas;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
