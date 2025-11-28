package com.xime.averapizza.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @JsonIgnore
    @OneToMany(mappedBy = "sabor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrecioSaborPresentacion> precios;

    @OneToOne(mappedBy = "sabor", cascade = CascadeType.ALL)
    private Receta receta;
}
