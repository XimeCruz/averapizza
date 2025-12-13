package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "receta_detalle")
@Data
public class RecetaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;

    private Double cantidad;

    @ManyToOne
    @JoinColumn(name = "receta_id")
    private Receta receta;

    @ManyToOne
    @JoinColumn(name = "presentacion_id", nullable = false)
    private PresentacionProducto presentacion;
}
