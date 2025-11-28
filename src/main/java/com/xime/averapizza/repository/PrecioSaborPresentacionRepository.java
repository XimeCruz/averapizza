package com.xime.averapizza.repository;

import com.xime.averapizza.model.PrecioSaborPresentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrecioSaborPresentacionRepository extends JpaRepository<PrecioSaborPresentacion, Long> {

    Optional<PrecioSaborPresentacion> findBySaborIdAndPresentacionId(Long saborId, Long presentacionId);

    List<PrecioSaborPresentacion> findBySaborId(Long saborId);
}
