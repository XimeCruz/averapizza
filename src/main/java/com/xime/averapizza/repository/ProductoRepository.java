package com.xime.averapizza.repository;

import com.xime.averapizza.dto.ProductoCompletoDTO;
import com.xime.averapizza.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Long countByActivoTrue();

    @Query("""
        SELECT new com.xime.averapizza.dto.ProductoCompletoDTO(
            p.id,
            p.nombre,
            CAST(p.tipoProducto AS string),
            s.nombre,
            CAST(pr.tipo AS string),
            psp.precio,
            s.id,
            pr.id
        )
        FROM Producto p
        INNER JOIN SaborPizza s ON s.producto.id = p.id
        INNER JOIN PrecioSaborPresentacion psp ON psp.sabor.id = s.id
        INNER JOIN PresentacionProducto pr ON pr.id = psp.presentacion.id
        WHERE p.activo = true
          AND s.activo = true
          AND pr.activo = true
        ORDER BY p.tipoProducto, s.nombre, pr.tipo
    """)
    List<ProductoCompletoDTO> findAllProductosCompletos();

    // Variante: Filtrar solo por tipo
    @Query("""
        SELECT new com.xime.averapizza.dto.ProductoCompletoDTO(
            p.id,
            p.nombre,
            CAST(p.tipoProducto AS string),
            s.nombre,
            CAST(pr.tipo AS string),
            psp.precio,
            s.id,
            pr.id
        )
        FROM Producto p
        INNER JOIN SaborPizza s ON s.producto.id = p.id
        INNER JOIN PrecioSaborPresentacion psp ON psp.sabor.id = s.id
        INNER JOIN PresentacionProducto pr ON pr.id = psp.presentacion.id
        WHERE p.activo = true
          AND s.activo = true
          AND pr.activo = true
          AND p.tipoProducto = :tipo
        ORDER BY s.nombre, pr.tipo
    """)
    List<ProductoCompletoDTO> findProductosByTipo(Producto.TipoProducto tipo);

}
