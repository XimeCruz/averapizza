package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "receta")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;

    private Double cantidad;

}
