package com.xime.averapizza.service.impl;

import com.xime.averapizza.model.Producto;
import com.xime.averapizza.repository.ProductoRepository;
import com.xime.averapizza.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;

    @Override
    public Producto crear(Producto producto) {
        return repository.save(producto);
    }

    @Override
    public Producto actualizar(Long id, Producto p) {
        Producto producto = obtener(id);
        producto.setNombre(p.getNombre());
        producto.setTipoProducto(p.getTipoProducto());
        producto.setTieneSabores(p.getTieneSabores());
        return repository.save(producto);
    }

    @Override
    public Producto obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Override
    public List<Producto> listar() {
        return repository.findAll();
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
