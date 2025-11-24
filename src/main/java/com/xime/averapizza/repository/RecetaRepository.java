package com.xime.averapizza.repository;

import com.xime.averapizza.model.Producto;
import com.xime.averapizza.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecetaRepository extends JpaRepository<Receta, Long> {

    //List<Receta> findByProducto(Producto producto);

    Optional<Receta> findByProducto(Producto producto);

}
