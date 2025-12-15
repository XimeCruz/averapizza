package com.xime.averapizza.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediccion_pedido")
@Data
public class PrediccionPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaPrediccion; // fecha para la que se predice

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "sabor_id")
    private SaborPizza sabor; // puede ser null si es bebida u otro

    @Enumerated(EnumType.STRING)
    private PresentacionProducto.TipoPresentacion tipoPresentacion;

    private Integer cantidadPredicha;

    private Double confianza; // porcentaje de confianza (0-100)

    private LocalDateTime fechaCalculo; // cuándo se hizo el cálculo

    @Enumerated(EnumType.STRING)
    private TipoPrediccion tipoPrediccion; // DIA_SEMANA, TENDENCIA, PROMEDIO

    private String metadatos; // JSON con info adicional

    public enum TipoPrediccion {
        DIA_SEMANA,      // basado en día de la semana
        TENDENCIA,       // basado en tendencia temporal
        PROMEDIO_SIMPLE, // promedio de últimos N días
        ESTACIONAL       // patrones estacionales
    }
}