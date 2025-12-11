package com.xime.averapizza.repository;

import com.xime.averapizza.model.DetallePedido;
import com.xime.averapizza.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    @Query("SELECT dp FROM DetallePedido dp " +
            "JOIN dp.pedido p " +
            "WHERE p.usuarioId = :usuarioId " +
            "AND dp.producto.tipoProducto = :tipoProducto " +
            "AND p.estado != 'CANCELADO'")
    List<DetallePedido> findByPedidoUsuarioIdAndProductoTipoProducto(
            @Param("usuarioId") Integer usuarioId,
            @Param("tipoProducto") Producto.TipoProducto tipoProducto
    );

}
