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

    @Enumerated(EnumType.STRING)
    private TipoProducto tipoProducto;

    // true para pizzas
    private Boolean tieneSabores;

    private Boolean activo = true;

    public enum TipoProducto {
        PIZZA,
        BEBIDA,
        OTRO
    }

}

