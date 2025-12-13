package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.InsumoCalculadoDTO;
import com.xime.averapizza.dto.RecetaDTO;
import com.xime.averapizza.dto.RecetaDetalleDTO;
import com.xime.averapizza.model.*;
import com.xime.averapizza.repository.*;
import com.xime.averapizza.service.RecetaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecetaServiceImpl implements RecetaService {

    private final RecetaRepository repo;
    private final RecetaDetalleRepository detalleRepo;
    private final SaborPizzaRepository saborRepo;
    private final InsumoRepository insumoRepo;
    private final RecetaDetalleRepository recetaDetalleRepository;

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


    @Override
    public List<InsumoCalculadoDTO> calcularInsumosParaDetalle(DetallePedido detalle) {

        if (detalle.getProducto().getTipoProducto() != Producto.TipoProducto.PIZZA) {
            return Collections.emptyList();
        }

        List<SaborPizza> sabores = detalle.getSabores();
        if (sabores.isEmpty()) {
            throw new RuntimeException("No hay sabores definidos en el detalle del pedido");
        }

        Long presentacionId = detalle.getPresentacion().getId();
        Integer numeroSabores = detalle.getNumeroSabores();
        Integer cantidadPizzas = detalle.getCantidad();

        // Obtener IDs de sabores
        List<Long> saborIds = sabores.stream()
                .map(SaborPizza::getId)
                .collect(Collectors.toList());

        // Obtener recetas de todos los sabores para esta presentación
        List<RecetaDetalle> recetasDetalles = recetaDetalleRepository
                .findByMultipleSaboresAndPresentacion(saborIds, presentacionId);

        if (recetasDetalles.isEmpty()) {
            throw new RuntimeException(
                    "No hay recetas configuradas para los sabores y presentación seleccionados"
            );
        }

        // Agrupar por insumo y sumar cantidades
        Map<Insumo, Double> insumosPorCantidad = new HashMap<>();

        for (RecetaDetalle rd : recetasDetalles) {
            // Cantidad por pizza = cantidadBase / numeroSabores * cantidadPizzas
            Double cantidadPorPizza = rd.getCantidad() / numeroSabores;
            Double cantidadTotal = cantidadPorPizza * cantidadPizzas;

            Insumo insumo = rd.getInsumo();
            insumosPorCantidad.merge(insumo, cantidadTotal, Double::sum);
        }

        // Convertir a DTO
        return insumosPorCantidad.entrySet().stream()
                .map(entry -> InsumoCalculadoDTO.builder()
                        .insumoId(entry.getKey().getId())
                        .nombreInsumo(entry.getKey().getNombre())
                        .cantidadNecesaria(entry.getValue())
                        .unidadMedida(entry.getKey().getUnidadMedida())
                        .stockActual(entry.getKey().getStockActual())
                        .build())
                .collect(Collectors.toList());
    }

}

