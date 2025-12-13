package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    public List<SaborPizza> getSabores() {
        List<SaborPizza> sabores = new ArrayList<>();
        if (sabor1 != null) sabores.add(sabor1);
        if (sabor2 != null) sabores.add(sabor2);
        if (sabor3 != null) sabores.add(sabor3);
        return sabores;
    }

    public int getNumeroSabores() {
        int count = 0;
        if (sabor1 != null) count++;
        if (sabor2 != null) count++;
        if (sabor3 != null) count++;
        return count > 0 ? count : 1;
    }

}

