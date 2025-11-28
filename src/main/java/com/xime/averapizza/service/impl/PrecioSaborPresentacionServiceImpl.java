package com.xime.averapizza.service.impl;

import com.xime.averapizza.model.PrecioSaborPresentacion;
import com.xime.averapizza.model.PresentacionProducto;
import com.xime.averapizza.model.SaborPizza;
import com.xime.averapizza.repository.PrecioSaborPresentacionRepository;
import com.xime.averapizza.repository.PresentacionProductoRepository;
import com.xime.averapizza.repository.SaborPizzaRepository;
import com.xime.averapizza.service.PrecioSaborPresentacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrecioSaborPresentacionServiceImpl implements PrecioSaborPresentacionService {

    private final PrecioSaborPresentacionRepository repo;
    private final SaborPizzaRepository saborRepo;
    private final PresentacionProductoRepository presentacionRepo;

    @Override
    public PrecioSaborPresentacion asignarPrecio(Long saborId, Long presentacionId, Double precio) {

        SaborPizza sabor = saborRepo.findById(saborId)
                .orElseThrow(() -> new RuntimeException("Sabor no encontrado"));

        PresentacionProducto presentacion = presentacionRepo.findById(presentacionId)
                .orElseThrow(() -> new RuntimeException("Presentación no existe"));

        PrecioSaborPresentacion p = repo
                .findBySaborIdAndPresentacionId(saborId, presentacionId)
                .orElse(new PrecioSaborPresentacion());

        p.setSabor(sabor);
        p.setPresentacion(presentacion);
        p.setPrecio(precio);

        return repo.save(p);
    }

    @Override
    public List<PrecioSaborPresentacion> listarPorSabor(Long saborId) {
        return repo.findBySaborId(saborId);
    }
}

