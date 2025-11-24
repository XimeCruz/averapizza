package com.xime.averapizza.repository;

import com.xime.averapizza.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}