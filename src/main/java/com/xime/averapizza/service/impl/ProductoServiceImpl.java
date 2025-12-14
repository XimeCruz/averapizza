package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.ProductoCompletoDTO;
import com.xime.averapizza.dto.ProductoMenuDTO;
import com.xime.averapizza.model.PrecioSaborPresentacion;
import com.xime.averapizza.model.PresentacionProducto;
import com.xime.averapizza.model.Producto;
import com.xime.averapizza.model.SaborPizza;
import com.xime.averapizza.repository.PrecioSaborPresentacionRepository;
import com.xime.averapizza.repository.PresentacionProductoRepository;
import com.xime.averapizza.repository.ProductoRepository;
import com.xime.averapizza.repository.SaborPizzaRepository;
import com.xime.averapizza.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;
    private final SaborPizzaRepository saborPizzaRepository;
    private final PresentacionProductoRepository presentacionRepository;
    private final PrecioSaborPresentacionRepository precioSaborPresentacionRepository;


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

    public List<ProductoCompletoDTO> obtenerTodosLosProductos() {
        return repository.findAllProductosCompletos();
    }

    public List<ProductoCompletoDTO> obtenerProductosPorTipo(String tipo) {
        Producto.TipoProducto tipoProducto = Producto.TipoProducto.valueOf(tipo.toUpperCase());
        return repository.findProductosByTipo(tipoProducto);
    }

    public Map<String, List<ProductoMenuDTO>> obtenerPizzasPorPresentacion() {
        Map<String, List<ProductoMenuDTO>> resultado = new LinkedHashMap<>();

        // Inicializar las listas
        resultado.put("PESO", new ArrayList<>());
        resultado.put("REDONDA", new ArrayList<>());
        resultado.put("BANDEJA", new ArrayList<>());

        // Obtener todos los sabores activos
        List<SaborPizza> sabores = saborPizzaRepository.findByActivoTrue();

        for (SaborPizza sabor : sabores) {
            // Obtener precios por presentación de este sabor
            List<PrecioSaborPresentacion> precios = precioSaborPresentacionRepository
                    .findBySaborIdAndPresentacionActivoTrue(sabor.getId());

            for (PrecioSaborPresentacion precio : precios) {
                PresentacionProducto presentacion = precio.getPresentacion();
                String tipoPresentacion = presentacion.getTipo().name();

                // Solo agregar si es PESO, REDONDA o BANDEJA
                if (tipoPresentacion.equals("PESO") ||
                        tipoPresentacion.equals("REDONDA") ||
                        tipoPresentacion.equals("BANDEJA")) {

                    ProductoMenuDTO dto = ProductoMenuDTO.builder()
                            .id(sabor.getId())
                            .nombre(sabor.getNombre())
                            .precio(precio.getPrecio())
                            .presentacion(tipoPresentacion)
                            .presentacionId(presentacion.getId())
                            .tipoProducto("PIZZA")
                            .build();

                    resultado.get(tipoPresentacion).add(dto);
                }
            }
        }

        return resultado;
    }

    /**
     * Obtiene todas las bebidas con sus presentaciones
     */
    public List<ProductoMenuDTO> obtenerBebidas() {
        List<ProductoMenuDTO> bebidas = new ArrayList<>();

        // Buscar productos tipo BEBIDA
        List<Producto> productosBebida = repository.findByTipoProductoAndActivoTrue(Producto.TipoProducto.BEBIDA);

        for (Producto producto : productosBebida) {
            // Obtener presentaciones activas de este producto
            List<PresentacionProducto> presentaciones = presentacionRepository
                    .findByProductoIdAndActivoTrue(Math.toIntExact(producto.getId()));

            for (PresentacionProducto presentacion : presentaciones) {
                ProductoMenuDTO dto = ProductoMenuDTO.builder()
                        .id(producto.getId())
                        .nombre(producto.getNombre() + " " + formatPresentacion(presentacion.getTipo()))
                        .precio(presentacion.getPrecioBase())
                        .presentacion(presentacion.getTipo().name())
                        .presentacionId(presentacion.getId())
                        .tipoProducto("BEBIDA")
                        .build();

                bebidas.add(dto);
            }
        }

        return bebidas;
    }

    private String formatPresentacion(PresentacionProducto.TipoPresentacion tipo) {
        return switch (tipo) {
            case ML_330 -> "330ml";
            case ML_750 -> "750ml";
            case L_2 -> "2L";
            case L_3 -> "3L";
            default -> "";
        };
    }
}
