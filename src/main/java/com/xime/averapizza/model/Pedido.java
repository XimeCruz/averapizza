package com.xime.averapizza.model;


import com.xime.averapizza.dto.DetallePedidoItem;
import com.xime.averapizza.dto.PedidoResponseDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private PedidoResponseDTO mapToResponse(Pedido pedido) {
        List<DetallePedidoItem> items = new ArrayList<>();

        if (pedido.getDetalles() != null) {
            for (DetallePedido det : pedido.getDetalles()) {
                items.add(
                        DetallePedidoItem.builder()
                                .productoNombre(det.getProducto().getNombre())
                                .cantidad(det.getCantidad())
                                .subtotal(det.getSubtotal())
                                .build()
                );
            }
        }

        return PedidoResponseDTO.builder()
                .pedidoId(pedido.getId())
                .total(pedido.getTotal())
                .estado(pedido.getEstado().name())
                .tipoServicio(pedido.getTipoServicio().name())
                .items(items)
                .build();
    }

}

