package com.xime.averapizza.service.impl;

import com.xime.averapizza.model.PresentacionProducto;
import com.xime.averapizza.repository.PresentacionProductoRepository;
import com.xime.averapizza.service.PresentacionProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresentacionProductoServiceImpl implements PresentacionProductoService {

    private final PresentacionProductoRepository repo;

    @Override
    public PresentacionProducto crear(PresentacionProducto p) {
        return repo.save(p);
    }

    @Override
    public List<PresentacionProducto> listar() {
        return repo.findAll();
    }

    @Override
    public PresentacionProducto obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentación no existe"));
    }

    @Override
    public List<PresentacionProducto> obtenerPorProductoId(Integer productoId) {
        return repo.findByProductoId(productoId);
    }
}

