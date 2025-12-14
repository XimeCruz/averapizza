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

    @Query(value = """
        SELECT 
            CAST(s.sabor_id AS BIGINT) as saborId,
            CAST(s.sabor_nombre AS VARCHAR) as saborNombre,
            CAST(s.presentacion_id AS BIGINT) as presentacionId,
            CAST(s.tipo_presentacion AS VARCHAR) as tipoPresentacion,
            CAST(s.cantidad_vendida AS DOUBLE PRECISION) as cantidadVendida,
            CAST(s.numero_ventas AS BIGINT) as numeroVentas,
            CAST(s.ingreso_total AS DOUBLE PRECISION) as ingresoTotal
        FROM (
            SELECT 
                s1.id as sabor_id,
                s1.nombre as sabor_nombre,
                pr.id as presentacion_id,
                pr.tipo as tipo_presentacion,
                COALESCE(SUM(CASE 
                    WHEN pr.tipo = 'PESO' THEN dp.peso_kg 
                    ELSE CAST(dp.cantidad AS DOUBLE PRECISION)
                END), 0.0) as cantidad_vendida,
                COUNT(dp.id) as numero_ventas,
                COALESCE(SUM(dp.subtotal), 0.0) as ingreso_total
            FROM detalle_pedido dp
            JOIN pedido p ON dp.pedido_id = p.id
            JOIN sabor_pizza s1 ON dp.sabor1_id = s1.id
            LEFT JOIN presentacion_producto pr ON dp.presentacion_id = pr.id
            WHERE p.estado = 'ENTREGADO'
              AND dp.sabor1_id IS NOT NULL
              AND (CAST(:fechaInicio AS TIMESTAMP) IS NULL OR p.fecha_hora >= CAST(:fechaInicio AS TIMESTAMP))
              AND (CAST(:fechaFin AS TIMESTAMP) IS NULL OR p.fecha_hora <= CAST(:fechaFin AS TIMESTAMP))
            GROUP BY s1.id, s1.nombre, pr.id, pr.tipo
            
            UNION ALL
            
            SELECT 
                s2.id as sabor_id,
                s2.nombre as sabor_nombre,
                pr.id as presentacion_id,
                pr.tipo as tipo_presentacion,
                COALESCE(SUM(CASE 
                    WHEN pr.tipo = 'PESO' THEN dp.peso_kg / 2.0
                    ELSE CAST(dp.cantidad AS DOUBLE PRECISION) / 2.0
                END), 0.0) as cantidad_vendida,
                COUNT(dp.id) as numero_ventas,
                COALESCE(SUM(dp.subtotal / 2.0), 0.0) as ingreso_total
            FROM detalle_pedido dp
            JOIN pedido p ON dp.pedido_id = p.id
            JOIN sabor_pizza s2 ON dp.sabor2_id = s2.id
            LEFT JOIN presentacion_producto pr ON dp.presentacion_id = pr.id
            WHERE p.estado = 'ENTREGADO'
              AND dp.sabor2_id IS NOT NULL
              AND (CAST(:fechaInicio AS TIMESTAMP) IS NULL OR p.fecha_hora >= CAST(:fechaInicio AS TIMESTAMP))
              AND (CAST(:fechaFin AS TIMESTAMP) IS NULL OR p.fecha_hora <= CAST(:fechaFin AS TIMESTAMP))
            GROUP BY s2.id, s2.nombre, pr.id, pr.tipo
            
            UNION ALL
            
            SELECT 
                s3.id as sabor_id,
                s3.nombre as sabor_nombre,
                pr.id as presentacion_id,
                pr.tipo as tipo_presentacion,
                COALESCE(SUM(CASE 
                    WHEN pr.tipo = 'PESO' THEN dp.peso_kg / 3.0
                    ELSE CAST(dp.cantidad AS DOUBLE PRECISION) / 3.0
                END), 0.0) as cantidad_vendida,
                COUNT(dp.id) as numero_ventas,
                COALESCE(SUM(dp.subtotal / 3.0), 0.0) as ingreso_total
            FROM detalle_pedido dp
            JOIN pedido p ON dp.pedido_id = p.id
            JOIN sabor_pizza s3 ON dp.sabor3_id = s3.id
            LEFT JOIN presentacion_producto pr ON dp.presentacion_id = pr.id
            WHERE p.estado = 'ENTREGADO'
              AND dp.sabor3_id IS NOT NULL
              AND (CAST(:fechaInicio AS TIMESTAMP) IS NULL OR p.fecha_hora >= CAST(:fechaInicio AS TIMESTAMP))
              AND (CAST(:fechaFin AS TIMESTAMP) IS NULL OR p.fecha_hora <= CAST(:fechaFin AS TIMESTAMP))
            GROUP BY s3.id, s3.nombre, pr.id, pr.tipo
        ) s
        ORDER BY s.cantidad_vendida DESC
    """, nativeQuery = true)
    List<Object[]> findSaboresVentasPorPresentacion(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );


    List<SaborPizza> findByActivoTrue();
    List<SaborPizza> findByProductoIdAndActivoTrue(Long productoId);
}

