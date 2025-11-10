package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tipo_servicio")
public class TipoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // Ej: "Para llevar", "Delivery", "En mesa"

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

}

