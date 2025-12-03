package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "presentacion_producto")
@Data
public class PresentacionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoPresentacion tipo;   // PESO, REDONDA, BANDEJA

    private Boolean usaPeso;

    private Integer maxSabores;

//    @ManyToOne
//    @JoinColumn(name = "producto_id")
//    private Producto producto;
//

    private Integer productoId;

    private Double precioBase;       // solo si aplica (redonda o bandeja) y BEBIDAS

    private Boolean activo = true;

    public enum TipoPresentacion {
        PESO,
        REDONDA,
        BANDEJA
    }

}

