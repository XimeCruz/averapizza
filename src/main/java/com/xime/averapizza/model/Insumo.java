package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "insumo")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String unidadMedida;
    private Double stockActual;
    private Double stockMinimo;

    private Boolean activo=true;

//    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL)
//    private List<MovimientoInventario> movimientos;

}

