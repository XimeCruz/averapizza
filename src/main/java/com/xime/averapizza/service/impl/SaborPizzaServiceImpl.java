package com.xime.averapizza.service.impl;

import com.xime.averapizza.model.Producto;
import com.xime.averapizza.model.SaborPizza;
import com.xime.averapizza.repository.ProductoRepository;
import com.xime.averapizza.repository.SaborPizzaRepository;
import com.xime.averapizza.service.SaborPizzaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaborPizzaServiceImpl implements SaborPizzaService {

    private final SaborPizzaRepository repo;
    private final ProductoRepository productoRepo;

    @Override
    public SaborPizza crear(SaborPizza sabor) {
        System.out.println(sabor);
        Producto producto = productoRepo.findById(sabor.getProducto().getId()).orElse(null);
        if(producto == null){
            new Exception("No se encontro el producto");
        }
        sabor.setProducto(producto);
        System.out.println(sabor);
        return repo.save(sabor);
    }

    @Override
    public SaborPizza actualizar(Long id, SaborPizza s) {
        SaborPizza sabor = obtener(id);
        sabor.setNombre(s.getNombre());
        sabor.setDescripcion(s.getDescripcion());
        return repo.save(sabor);
    }

    @Override
    public List<SaborPizza> listarPorProducto(Long productoId) {
        return repo.findByProductoId(productoId);
    }

    @Override
    public SaborPizza obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sabor no encontrado"));
    }
}
