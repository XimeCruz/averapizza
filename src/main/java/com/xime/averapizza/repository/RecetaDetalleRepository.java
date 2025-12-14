package com.xime.averapizza.repository;

import com.xime.averapizza.model.Producto;
import com.xime.averapizza.model.RecetaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecetaDetalleRepository extends JpaRepository<RecetaDetalle, Long> {

//    @Query("SELECT rd FROM RecetaDetalle rd WHERE rd.receta.producto = :producto")
//    List<RecetaDetalle> findByProducto(Producto producto);

    List<RecetaDetalle> findByRecetaId(Long recetaId);

    // Obtener insumos para un sabor específico y presentación
    @Query("SELECT rd FROM RecetaDetalle rd " +
            "WHERE rd.receta.sabor.id = :saborId " +
            "AND rd.presentacion.id = :presentacionId " +
            "AND rd.receta.activo = true")
    List<RecetaDetalle> findBySaborAndPresentacion(
            @Param("saborId") Long saborId,
            @Param("presentacionId") Long presentacionId
    );

    // Obtener insumos para múltiples sabores y una presentación
    @Query("SELECT rd FROM RecetaDetalle rd " +
            "WHERE rd.receta.sabor.id IN :saborIds " +
            "AND rd.presentacion.id = :presentacionId " +
            "AND rd.receta.activo = true")
    List<RecetaDetalle> findByMultipleSaboresAndPresentacion(
            @Param("saborIds") List<Long> saborIds,
            @Param("presentacionId") Long presentacionId
    );


    // Eliminar detalles de una receta
    void deleteByRecetaId(Long recetaId);

}

