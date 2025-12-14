package com.xime.averapizza.repository;

import com.xime.averapizza.model.SaborPizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaborPizzaRepository extends JpaRepository<SaborPizza, Long> {
    List<SaborPizza> findByProductoId(Long productoId);

    Long countByActivoTrue();

    @Query("""
        SELECT dp.sabor1.id as saborId,
               dp.sabor1.nombre as saborNombre,
               dp.presentacion.id as presentacionId,
               dp.presentacion.tipo as tipoPresentacion,
               COALESCE(SUM(CASE 
                   WHEN dp.presentacion.tipo = 'PESO' THEN dp.pesoKg 
                   ELSE dp.cantidad 
               END), 0) as cantidadVendida,
               COUNT(dp.id) as numeroVentas,
               COALESCE(SUM(dp.subtotal), 0) as ingresoTotal
        FROM DetallePedido dp
        WHERE dp.pedido.estado = 'ENTREGADO'
          AND dp.sabor1 IS NOT NULL
          AND (:fechaInicio IS NULL OR dp.pedido.fechaHora >= :fechaInicio)
          AND (:fechaFin IS NULL OR dp.pedido.fechaHora <= :fechaFin)
        GROUP BY dp.sabor1.id, dp.sabor1.nombre, dp.presentacion.id, dp.presentacion.tipo
        
        UNION ALL
        
        SELECT dp.sabor2.id as saborId,
               dp.sabor2.nombre as saborNombre,
               dp.presentacion.id as presentacionId,
               dp.presentacion.tipo as tipoPresentacion,
               COALESCE(SUM(CASE 
                   WHEN dp.presentacion.tipo = 'PESO' THEN dp.pesoKg / 2.0
                   ELSE dp.cantidad / 2.0
               END), 0) as cantidadVendida,
               COUNT(dp.id) as numeroVentas,
               COALESCE(SUM(dp.subtotal / 2.0), 0) as ingresoTotal
        FROM DetallePedido dp
        WHERE dp.pedido.estado = 'ENTREGADO'
          AND dp.sabor2 IS NOT NULL
          AND (:fechaInicio IS NULL OR dp.pedido.fechaHora >= :fechaInicio)
          AND (:fechaFin IS NULL OR dp.pedido.fechaHora <= :fechaFin)
        GROUP BY dp.sabor2.id, dp.sabor2.nombre, dp.presentacion.id, dp.presentacion.tipo
        
        UNION ALL
        
        SELECT dp.sabor3.id as saborId,
               dp.sabor3.nombre as saborNombre,
               dp.presentacion.id as presentacionId,
               dp.presentacion.tipo as tipoPresentacion,
               COALESCE(SUM(CASE 
                   WHEN dp.presentacion.tipo = 'PESO' THEN dp.pesoKg / 3.0
                   ELSE dp.cantidad / 3.0
               END), 0) as cantidadVendida,
               COUNT(dp.id) as numeroVentas,
               COALESCE(SUM(dp.subtotal / 3.0), 0) as ingresoTotal
        FROM DetallePedido dp
        WHERE dp.pedido.estado = 'ENTREGADO'
          AND dp.sabor3 IS NOT NULL
          AND (:fechaInicio IS NULL OR dp.pedido.fechaHora >= :fechaInicio)
          AND (:fechaFin IS NULL OR dp.pedido.fechaHora <= :fechaFin)
        GROUP BY dp.sabor3.id, dp.sabor3.nombre, dp.presentacion.id, dp.presentacion.tipo
        
        ORDER BY cantidadVendida DESC
    """)
    List<Object[]> findSaboresVentasPorPresentacion(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}

