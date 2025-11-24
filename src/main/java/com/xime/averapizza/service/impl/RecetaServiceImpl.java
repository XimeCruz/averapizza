package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.CrearRecetaRequestDTO;
import com.xime.averapizza.dto.RecetaDetalleItemDTO;
import com.xime.averapizza.model.Insumo;
import com.xime.averapizza.model.Producto;
import com.xime.averapizza.model.Receta;
import com.xime.averapizza.model.RecetaDetalle;
import com.xime.averapizza.repository.InsumoRepository;
import com.xime.averapizza.repository.ProductoRepository;
import com.xime.averapizza.repository.RecetaDetalleRepository;
import com.xime.averapizza.repository.RecetaRepository;
import com.xime.averapizza.service.RecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RecetaServiceImpl implements RecetaService {

    private final ProductoRepository productoRepository;
    private final InsumoRepository insumoRepository;
    private final RecetaRepository recetaRepository;
    private final RecetaDetalleRepository recetaDetalleRepository;

    @Override
    public Receta crearOActualizarReceta(CrearRecetaRequestDTO request) {

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Buscar receta existente o crear nueva
        Receta receta = recetaRepository.findByProducto(producto).orElseGet(() -> {
            Receta r = new Receta();
            r.setProducto(producto);
            r.setActivo(true);
            r.setDetalles(new ArrayList<>());
            return r;
        });

        // Borrar detalles anteriores
        if (receta.getId() != null) {
            recetaDetalleRepository.deleteAll(receta.getDetalles());
            receta.getDetalles().clear();
        }

        // Crear nuevos detalles
        for (RecetaDetalleItemDTO item : request.getItems()) {
            Insumo insumo = insumoRepository.findById(item.getInsumoId())
                    .orElseThrow(() -> new RuntimeException("Insumo no encontrado: " + item.getInsumoId()));

            RecetaDetalle det = new RecetaDetalle();
            det.setReceta(receta);
            det.setInsumo(insumo);
            det.setCantidad(item.getCantidad());

            receta.getDetalles().add(det);
        }

        // Marcar producto como conReceta = true
        producto.setConReceta(true);
        productoRepository.save(producto);

        return recetaRepository.save(receta);
    }
}
