package com.xime.averapizza.repository;

import com.xime.averapizza.model.PrediccionPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrediccionPedidoRepository extends JpaRepository<PrediccionPedido, Long> {

    List<PrediccionPedido> findByFechaPrediccion(LocalDate fecha);

    List<PrediccionPedido> findByFechaPrediccionBetween(LocalDate inicio, LocalDate fin);

    @Query("SELECT p FROM PrediccionPedido p WHERE p.fechaPrediccion = :fecha AND p.producto.id = :productoId")
    List<PrediccionPedido> findByFechaAndProducto(
            @Param("fecha") LocalDate fecha,
            @Param("productoId") Long productoId
    );

    void deleteByFechaPrediccionBefore(LocalDate fecha);
}