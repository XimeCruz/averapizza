package com.xime.averapizza.repository;

import com.xime.averapizza.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {


    @Query("""
        SELECT i FROM Insumo i
        WHERE i.stockActual <= i.stockMinimo
    """)
    List<Insumo> findInsumosBajoStock();

    @Query("SELECT i FROM Insumo i WHERE i.stockActual <= i.stockMinimo AND i.activo = true")
    List<Insumo> findByStockActualLessThanEqualStockMinimo();

}
