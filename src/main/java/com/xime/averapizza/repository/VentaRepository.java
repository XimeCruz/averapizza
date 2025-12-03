package com.xime.averapizza.repository;

import com.xime.averapizza.dto.VentasPorTipoDTO;
import com.xime.averapizza.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("""
    SELECT v FROM Venta v
    WHERE v.fecha BETWEEN :inicio AND :fin
""")
    List<Venta> ventasEntreFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );


    @Query("""
        SELECT dv.producto.nombre, SUM(dv.cantidad) 
        FROM DetalleVenta dv 
        GROUP BY dv.producto.nombre 
        ORDER BY SUM(dv.cantidad) DESC
    """)
    List<Object[]> productosMasVendidos();


    @Query("""
        SELECT new com.xime.averapizza.dto.VentasPorTipoDTO(
            p.tipoServicio,
            COUNT(v.id),
            SUM(v.total)
        )
        FROM Venta v
        JOIN v.pedido p
        WHERE v.fecha BETWEEN :inicio AND :fin
        GROUP BY p.tipoServicio
        """)
    List<VentasPorTipoDTO> obtenerVentasPorTipo(LocalDateTime inicio, LocalDateTime fin);
}