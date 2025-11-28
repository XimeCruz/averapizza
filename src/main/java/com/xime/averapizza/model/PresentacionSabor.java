package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "presentacion_sabor")
@Data
public class PresentacionSabor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "presentacion_id")
    private PresentacionProducto presentacion;

    @ManyToOne
    @JoinColumn(name = "sabor_id")
    private SaborPizza sabor;

    private Integer orden;  // 1,2,3 para las combinaciones
}

