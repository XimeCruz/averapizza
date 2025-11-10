package com.xime.averapizza.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoServicio; // "En mesa", "Delivery", "Para llevar"
    private String estado;       // "Pendiente", "En preparación", "Entregado"
    private LocalDateTime fechaHora = LocalDateTime.now();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;

    @OneToOne(mappedBy = "pedido")
    private Venta venta;

}

