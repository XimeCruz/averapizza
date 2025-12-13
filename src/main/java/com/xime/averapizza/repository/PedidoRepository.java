package com.xime.averapizza.repository;

import com.xime.averapizza.model.EstadoPedido;
import com.xime.averapizza.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    Long countByEstado(EstadoPedido estado);

    Long countByUsuarioId(Integer usuarioId);

    List<Pedido> findByUsuarioIdAndEstadoNot(Integer usuarioId, EstadoPedido estado);

    List<Pedido> findByUsuarioIdOrderByFechaHoraDesc(Integer usuarioId);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = :estado " +
            "AND p.fechaHora BETWEEN :inicio AND :fin")
    Integer countByEstadoAndFechaHoraBetween(
            @Param("estado") EstadoPedido estado,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
}
