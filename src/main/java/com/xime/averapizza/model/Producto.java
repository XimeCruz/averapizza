package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
//    private String descripcion;
    private Double precio;
    private Boolean conReceta;

//    @Column(name = "con_receta")
//    private Boolean conReceta;

//    @ManyToOne
//    @JoinColumn(name = "sucursal_id")
//    private Sucursal sucursal;
//
//    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Receta> recetas;

}

