package com.xime.averapizza.repository;

import com.xime.averapizza.model.Producto;
import com.xime.averapizza.model.Receta;
import com.xime.averapizza.model.SaborPizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecetaRepository extends JpaRepository<Receta, Long> {

    //List<Receta> findByProducto(Producto producto);

    //Optional<Receta> findByProducto(Producto producto);

    Optional<Receta> findBySabor(SaborPizza sabor);

    Optional<Receta> findBySaborId(Long saborId);

//    @Query("SELECT r FROM Receta r WHERE r.sabor.id IN :saborIds AND r. = :presentacionId")
//    List<Receta> findBySaborIdsAndPresentacionId(
//            @Param("saborIds") List<Long> saborIds,
//            @Param("presentacionId") Long presentacionId
//    );

    boolean existsBySaborId(Long saborId);

    @Query("SELECT r FROM Receta r LEFT JOIN FETCH r.detalles WHERE r.sabor.id = :saborId")
    Optional<Receta> findBySaborIdWithDetalles(@Param("saborId") Long saborId);
}
