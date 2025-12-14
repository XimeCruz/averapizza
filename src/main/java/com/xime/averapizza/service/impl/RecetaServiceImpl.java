package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.*;
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

    private final RecetaRepository recetaRepository;
    private final RecetaDetalleRepository detalleRepo;
    private final SaborPizzaRepository saborRepository;
    private final InsumoRepository insumoRepository;
    private final RecetaDetalleRepository recetaDetalleRepository;
    private final PresentacionProductoRepository presentacionRepository;

    @Override
    public boolean tieneReceta(Long saborId) {
        return recetaRepository.existsBySaborId(saborId);
    }

    @Override
    public RecetaDTO obtenerPorSabor(Long saborId) {
        Receta receta = recetaRepository.findBySaborIdWithDetalles(saborId)
                .orElseThrow(() -> new RuntimeException("El sabor no tiene receta configurada"));

        return convertirADTO(receta);
    }

    @Override
    public RecetaDTO crear(CrearRecetaRequest request) {
        // Verificar que el sabor existe
        SaborPizza sabor = saborRepository.findById(request.getSaborId())
                .orElseThrow(() -> new RuntimeException("Sabor no encontrado"));

        // Verificar que no tenga receta ya
        if (tieneReceta(request.getSaborId())) {
            throw new RuntimeException("Este sabor ya tiene una receta. Use actualizar en su lugar.");
        }

        // Crear receta
        Receta receta = new Receta();
        receta.setSabor(sabor);
        receta.setActivo(true);
        receta.setDetalles(new ArrayList<>());
        receta = recetaRepository.save(receta);

        // Crear detalles
        for (DetalleInsumoRequest detRequest : request.getDetalles()) {
            agregarDetalle(receta, detRequest);
        }

        return convertirADTO(receta);
    }

    @Override
    public RecetaDTO actualizar(Long recetaId, CrearRecetaRequest request) {
        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        // Eliminar detalles anteriores
        recetaDetalleRepository.deleteByRecetaId(recetaId);
        receta.getDetalles().clear();

        // Agregar nuevos detalles
        for (DetalleInsumoRequest detRequest : request.getDetalles()) {
            agregarDetalle(receta, detRequest);
        }

        receta = recetaRepository.save(receta);
        return convertirADTO(receta);
    }

    @Override
    public void eliminar(Long recetaId) {
        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        // Soft delete
        receta.setActivo(false);
        recetaRepository.save(receta);

        // O hard delete:
        // recetaRepository.deleteById(recetaId);
    }

    @Override
    public List<RecetaDTO> listarTodas() {
        return recetaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ========== MÉTODOS AUXILIARES ==========

    private void agregarDetalle(Receta receta, DetalleInsumoRequest detRequest) {
        Insumo insumo = insumoRepository.findById(detRequest.getInsumoId())
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado: " + detRequest.getInsumoId()));

        PresentacionProducto presentacion = presentacionRepository.findById(detRequest.getPresentacionId())
                .orElseThrow(() -> new RuntimeException("Presentación no encontrada: " + detRequest.getPresentacionId()));

        RecetaDetalle detalle = new RecetaDetalle();
        detalle.setReceta(receta);
        detalle.setInsumo(insumo);
        detalle.setCantidad(detRequest.getCantidad());
        detalle.setPresentacion(presentacion);

        receta.getDetalles().add(detalle);
        recetaDetalleRepository.save(detalle);
    }

    private RecetaDTO convertirADTO(Receta receta) {
        List<RecetaDetalleDTO> detallesDTO = receta.getDetalles().stream()
                .map(this::convertirDetalleADTO)
                .collect(Collectors.toList());

        return RecetaDTO.builder()
                .id(receta.getId())
                .saborId(receta.getSabor().getId())
                .saborNombre(receta.getSabor().getNombre())
                .activo(receta.isActivo())
                .detalles(detallesDTO)
                .build();
    }

    private RecetaDetalleDTO convertirDetalleADTO(RecetaDetalle detalle) {
        return RecetaDetalleDTO.builder()
                .id(detalle.getId())
                .insumoId(detalle.getInsumo().getId())
                .insumoNombre(detalle.getInsumo().getNombre())
                .unidadMedida(detalle.getInsumo().getUnidadMedida())
                .cantidad(detalle.getCantidad())
                .presentacionId(detalle.getPresentacion().getId())
                .presentacionTipo(detalle.getPresentacion().getTipo().name())
                .build();
    }

    @Override
    @Transactional
    public RecetaDTO crearOActualizar(Long saborId, List<RecetaDetalle> detallesRequest) {

        SaborPizza sabor = saborRepository.findById(saborId)
                .orElseThrow(() -> new RuntimeException("Sabor no encontrado"));

        Receta receta = recetaRepository.findBySaborId(saborId).orElseGet(() -> {
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
            Insumo insumo = insumoRepository.findById(d.getInsumo().getId())
                    .orElseThrow(() -> new RuntimeException("Insumo no encontrado"));

            RecetaDetalle nd = new RecetaDetalle();
            nd.setReceta(receta);
            nd.setInsumo(insumo);
            nd.setCantidad(d.getCantidad());

            receta.getDetalles().add(nd);
        }

        Receta guardada = recetaRepository.save(receta);

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

