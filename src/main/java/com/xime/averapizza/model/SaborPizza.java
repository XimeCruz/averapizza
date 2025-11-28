package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "sabor_pizza")
@Data
public class SaborPizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    // A qué producto pertenece (ej: Pizza Artesanal)
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @OneToMany(mappedBy = "sabor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrecioSaborPresentacion> precios;

    @OneToOne(mappedBy = "sabor", cascade = CascadeType.ALL)
    private Receta receta;
}
