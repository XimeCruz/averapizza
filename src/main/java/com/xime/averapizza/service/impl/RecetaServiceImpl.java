package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.RecetaDTO;
import com.xime.averapizza.dto.RecetaDetalleDTO;
import com.xime.averapizza.model.*;
import com.xime.averapizza.repository.*;
import com.xime.averapizza.service.RecetaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecetaServiceImpl implements RecetaService {

    private final RecetaRepository repo;
    private final RecetaDetalleRepository detalleRepo;
    private final SaborPizzaRepository saborRepo;
    private final InsumoRepository insumoRepo;

    @Override
    @Transactional
    public RecetaDTO crearOActualizar(Long saborId, List<RecetaDetalle> detallesRequest) {

        SaborPizza sabor = saborRepo.findById(saborId)
                .orElseThrow(() -> new RuntimeException("Sabor no encontrado"));

        Receta receta = repo.findBySaborId(saborId).orElseGet(() -> {
            Receta r = new Receta();
            r.setSabor(sabor);
            r.setDetalles(new ArrayList<>());
            return r;
        });

        // Limpia detalles anteriores
        if (receta.getId() != null) {
            detalleRepo.deleteAll(receta.getDetalles());
            receta.getDetalles().clear();
        }

        // Agregar nuevos detalles
        for (RecetaDetalle d : detallesRequest) {
            Insumo insumo = insumoRepo.findById(d.getInsumo().getId())
                    .orElseThrow(() -> new RuntimeException("Insumo no encontrado"));

            RecetaDetalle nd = new RecetaDetalle();
            nd.setReceta(receta);
            nd.setInsumo(insumo);
            nd.setCantidad(d.getCantidad());

            receta.getDetalles().add(nd);
        }

        Receta guardada = repo.save(receta);

        return mapToDto(guardada);
    }

    private RecetaDTO mapToDto(Receta receta) {
        RecetaDTO dto = new RecetaDTO();
        dto.setId(receta.getId());

        if (receta.getSabor() != null) {
            dto.setSaborId(receta.getSabor().getId());
            dto.setSaborNombre(receta.getSabor().getNombre());
        }

        List<RecetaDetalleDTO> detallesDto = receta.getDetalles().stream()
                .map(this::mapDetalleToDto)
                .toList();

        dto.setDetalles(detallesDto);

        return dto;
    }

    private RecetaDetalleDTO mapDetalleToDto(RecetaDetalle d) {
        RecetaDetalleDTO dto = new RecetaDetalleDTO();
        dto.setId(d.getId());
        dto.setCantidad(d.getCantidad());

        if (d.getInsumo() != null) {
            dto.setInsumoId(d.getInsumo().getId());
            dto.setInsumoNombre(d.getInsumo().getNombre());
            dto.setUnidadMedida(d.getInsumo().getUnidadMedida());
        }

        return dto;
    }

    @Override
    public Receta obtenerPorSabor(Long saborId) {
        return repo.findBySaborId(saborId)
                .orElseThrow(() -> new RuntimeException("Este sabor no tiene receta"));
    }
}

