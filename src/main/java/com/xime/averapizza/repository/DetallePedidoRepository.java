package com.xime.averapizza.repository;

import com.xime.averapizza.model.DetallePedido;
import com.xime.averapizza.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query("SELECT dp FROM DetallePedido dp " +
            "JOIN dp.pedido p " +
            "WHERE p.fechaHora BETWEEN :inicio AND :fin " +
            "AND p.estado != 'CANCELADO'")
    List<DetallePedido> findByFechaHoraBetween(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("SELECT dp.producto.id, SUM(dp.cantidad) " +
            "FROM DetallePedido dp " +
            "JOIN dp.pedido p " +
            "WHERE p.fechaHora BETWEEN :inicio AND :fin " +
            "AND p.estado != 'CANCELADO' " +
            "GROUP BY dp.producto.id")
    List<Object[]> findVentasPorProducto(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("SELECT FUNCTION('DAYOFWEEK', p.fechaHora), dp.producto.id, dp.sabor1.id, " +
            "dp.presentacion.tipo, AVG(dp.cantidad) " +
            "FROM DetallePedido dp " +
            "JOIN dp.pedido p " +
            "WHERE p.fechaHora BETWEEN :inicio AND :fin " +
            "AND p.estado != 'CANCELADO' " +
            "GROUP BY FUNCTION('DAYOFWEEK', p.fechaHora), dp.producto.id, " +
            "dp.sabor1.id, dp.presentacion.tipo")
    List<Object[]> findPromedioVentasPorDiaSemana(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

}
