package com.xime.averapizza.repository;

import com.xime.averapizza.model.SaborPizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaborPizzaRepository extends JpaRepository<SaborPizza, Long> {
    List<SaborPizza> findByProductoId(Long productoId);
}

