package com.xime.averapizza.repository;

import com.xime.averapizza.model.PresentacionSabor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresentacionSaborRepository extends JpaRepository<PresentacionSabor, Long> {
    List<PresentacionSabor> findByPresentacionId(Long presentacionId);
}

