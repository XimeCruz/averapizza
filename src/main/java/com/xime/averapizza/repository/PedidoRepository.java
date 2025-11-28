package com.xime.averapizza.repository;

import com.xime.averapizza.model.EstadoPedido;
import com.xime.averapizza.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

}
