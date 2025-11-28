package com.xime.averapizza.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "precio_sabor_presentacion")
public class PrecioSaborPresentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sabor_id")
    @JsonIgnore
    private SaborPizza sabor;

    @ManyToOne
    @JoinColumn(name = "presentacion_id")
    private PresentacionProducto presentacion;

    private Double precio; // precio por unidad, kg, redonda o bandeja
}
