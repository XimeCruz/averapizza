package com.xime.averapizza.service;

import com.xime.averapizza.model.PrecioSaborPresentacion;
import com.xime.averapizza.model.PresentacionProducto;
import com.xime.averapizza.model.SaborPizza;
import com.xime.averapizza.repository.PrecioSaborPresentacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class PizzaPricingService {

    @Autowired
    private PrecioSaborPresentacionRepository precioSaborPresentacionRepository;


    /**
     * Calcula el precio de una pizza según:
     * - PESO: precio por kg del sabor (promediado si hay varios)
     * - REDONDA: precio de la presentación del sabor (promediado si hay 2 sabores)
     * - BANDEJA: precio de la presentación del sabor (promediado si hay 2 o 3 sabores)
     */
    public Double calcularPrecio(
            PresentacionProducto presentacion,
            SaborPizza sabor1,
            SaborPizza sabor2,
            SaborPizza sabor3,
            Double pesoKg
    ) {
        PresentacionProducto.TipoPresentacion tipo = presentacion.getTipo();

        switch (tipo) {
            case PESO:
                return calcularPrecioPorPeso(presentacion, sabor1, sabor2, sabor3, pesoKg);

            case REDONDA:
                return calcularPrecioRedonda(presentacion, sabor1, sabor2);

            case BANDEJA:
                return calcularPrecioBandeja(presentacion, sabor1, sabor2, sabor3);

            default:
                throw new RuntimeException("Tipo de presentación no válido para pizzas: " + tipo);
        }
    }

    /**
     * Calcula precio por peso (kg)
     * Solo permite UN sabor para pizza por peso
     */
    private Double calcularPrecioPorPeso(
            PresentacionProducto presentacion,
            SaborPizza sabor1,
            SaborPizza sabor2,
            SaborPizza sabor3,
            Double pesoKg
    ) {
        if (pesoKg == null || pesoKg <= 0) {
            throw new RuntimeException("Peso inválido para pizza por peso");
        }

        // Validar que solo haya un sabor
        if (sabor2 != null || sabor3 != null) {
            throw new RuntimeException("Pizza por peso solo permite UN sabor");
        }

        // Obtener precio por kg del sabor
        Double precioPorKg = obtenerPrecioPorSaborYPresentacion(sabor1, presentacion);
        System.out.println("PESO  " + precioPorKg);

        // Retornar precio total según el peso
        return precioPorKg * pesoKg;
    }

    /**
     * Calcula precio para pizza redonda (máximo 2 sabores)
     * Promedia los precios de los sabores
     */
    private Double calcularPrecioRedonda(
            PresentacionProducto presentacion,
            SaborPizza sabor1,
            SaborPizza sabor2
    ) {
        List<Double> precios = new ArrayList<>();

        // Precio del sabor1
        Double precio1 = obtenerPrecioPorSaborYPresentacion(sabor1, presentacion);
        precios.add(precio1);

        // Si hay sabor2, agregar su precio
        if (sabor2 != null) {
            Double precio2 = obtenerPrecioPorSaborYPresentacion(sabor2, presentacion);
            precios.add(precio2);
        }

        System.out.println("REDONDA  " + precios);

        // Calcular y retornar promedio
        return precios.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow(() -> new RuntimeException("Error al calcular precio de pizza redonda"));
    }

    /**
     * Calcula precio para bandeja (máximo 3 sabores)
     * Promedia los precios de los sabores
     */
    private Double calcularPrecioBandeja(
            PresentacionProducto presentacion,
            SaborPizza sabor1,
            SaborPizza sabor2,
            SaborPizza sabor3
    ) {
        List<Double> precios = new ArrayList<>();

        // Precio del sabor1
        Double precio1 = obtenerPrecioPorSaborYPresentacion(sabor1, presentacion);
        precios.add(precio1);

        // Si hay sabor2, agregar su precio
        if (sabor2 != null) {
            Double precio2 = obtenerPrecioPorSaborYPresentacion(sabor2, presentacion);
            System.out.println(precio2);
            precios.add(precio2);
        }

        // Si hay sabor3, agregar su precio
        if (sabor3 != null) {
            Double precio3 = obtenerPrecioPorSaborYPresentacion(sabor3, presentacion);
            System.out.println(precio3);
            precios.add(precio3);
        }

        System.out.println("BANDEJA " + precios);

        // Calcular y retornar promedio
        return precios.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow(() -> new RuntimeException("Error al calcular precio de bandeja"));
    }

    /**
     * Obtiene el precio de un sabor para una presentación específica
     * desde la tabla precio_sabor_presentacion
     */
    private Double obtenerPrecioPorSaborYPresentacion(
            SaborPizza sabor,
            PresentacionProducto presentacion
    ) {
        PrecioSaborPresentacion precioSaborPresentacion =
                precioSaborPresentacionRepository.findBySaborAndPresentacion(sabor, presentacion)
                        .orElseThrow(() -> new RuntimeException(
                                "No se encontró precio para el sabor '" + sabor.getNombre() +
                                        "' en la presentación '" + presentacion.getTipo() + "'"
                        ));

        if (precioSaborPresentacion.getPrecio() == null || precioSaborPresentacion.getPrecio() <= 0) {
            throw new RuntimeException(
                    "Precio inválido para el sabor '" + sabor.getNombre() +
                            "' en la presentación '" + presentacion.getTipo() + "'"
            );
        }

        return precioSaborPresentacion.getPrecio();
    }
}



