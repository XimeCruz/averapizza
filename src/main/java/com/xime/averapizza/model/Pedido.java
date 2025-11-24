package com.xime.averapizza.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String tipoServicio; // "En mesa", "Delivery", "Para llevar"
//    private String estado;       // "Pendiente", "En preparación", "Entregado"

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @Enumerated(EnumType.STRING)
    private TipoServicio tipoServicio;

    private LocalDateTime fechaHora = LocalDateTime.now();

    //@Column(precision = 10, scale = 2)
    private Double total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;

//    @OneToOne(mappedBy = "pedido")
//    private Venta venta;

    @Column(name = "usuario_id")
    private Integer usuarioId;

}

