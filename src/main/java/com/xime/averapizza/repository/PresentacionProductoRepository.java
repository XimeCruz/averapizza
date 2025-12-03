package com.xime.averapizza.repository;

import com.xime.averapizza.model.PresentacionProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresentacionProductoRepository extends JpaRepository<PresentacionProducto, Long> {

    //List<PresentacionProducto> findByProductoId(Long productoId);

    //Optional<PresentacionProducto> findByNombre(String nombre);

    List<PresentacionProducto> findByProductoId(Integer productoId);

}
