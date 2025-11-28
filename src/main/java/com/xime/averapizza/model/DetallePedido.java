package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;

    //@Column(precision = 10, scale = 2)
    private Double subtotal;

    private Double precioUnitario;

    @ManyToOne
    @JoinColumn(name = "presentacion_id")
    private PresentacionProducto presentacion;

    @ManyToOne
    @JoinColumn(name = "sabor1_id")
    private SaborPizza sabor1;

    @ManyToOne
    @JoinColumn(name = "sabor2_id")
    private SaborPizza sabor2; // null si no hay

    @ManyToOne
    @JoinColumn(name = "sabor3_id")
    private SaborPizza sabor3; // solo para bandeja

    private Double pesoKg; // solo si presentacion = PESO


}

