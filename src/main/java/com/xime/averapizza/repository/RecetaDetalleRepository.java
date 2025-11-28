package com.xime.averapizza.repository;

import com.xime.averapizza.model.Producto;
import com.xime.averapizza.model.RecetaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecetaDetalleRepository extends JpaRepository<RecetaDetalle, Long> {

//    @Query("SELECT rd FROM RecetaDetalle rd WHERE rd.receta.producto = :producto")
//    List<RecetaDetalle> findByProducto(Producto producto);

    List<RecetaDetalle> findByRecetaId(Long recetaId);

}

