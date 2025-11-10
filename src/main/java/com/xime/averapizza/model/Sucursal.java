package com.xime.averapizza.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "sucursal")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;

    @OneToMany(mappedBy = "sucursal")
    private List<TipoServicio> tiposServicio;

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
    private List<Producto> productos;

}

