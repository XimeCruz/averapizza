package com.xime.averapizza.service;

import com.xime.averapizza.model.PrecioSaborPresentacion;
import com.xime.averapizza.model.PresentacionProducto;
import com.xime.averapizza.model.SaborPizza;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class PizzaPricingService {

    public Double calcularPrecio(
            PresentacionProducto presentacion,
            SaborPizza s1,
            SaborPizza s2,
            SaborPizza s3,
            Double pesoKg
    ) {

        PresentacionProducto.TipoPresentacion tipo = presentacion.getTipo();
        if (tipo == null) throw new RuntimeException("La presentación no tiene tipo.");

        switch (tipo) {

            case PESO:
                if (pesoKg == null || pesoKg <= 0)
                    throw new RuntimeException("Para PESO debe enviar pesoKg.");

                Double precioKg = obtenerPrecioPorPresentacion(s1, PresentacionProducto.TipoPresentacion.PESO);
                return pesoKg * precioKg;

            case REDONDA:
                return calcularPromedioPrecios(
                        obtenerPrecioPorPresentacion(s1, PresentacionProducto.TipoPresentacion.REDONDA),
                        s2 != null && s2.getId() != null && s2.getId() != 0
                                ? obtenerPrecioPorPresentacion(s2, PresentacionProducto.TipoPresentacion.REDONDA)
                                : null
                );
//                return maxPrecio(
//                        obtenerPrecioPorPresentacion(s1, PresentacionProducto.TipoPresentacion.REDONDA),
//                        s2 != null ? obtenerPrecioPorPresentacion(s2, PresentacionProducto.TipoPresentacion.REDONDA) : null
//                );

            case BANDEJA:
                return calcularPromedioPrecios(
                        obtenerPrecioPorPresentacion(s1, PresentacionProducto.TipoPresentacion.BANDEJA),
                        s2 != null && s2.getId() != null && s2.getId() != 0
                                ? obtenerPrecioPorPresentacion(s2, PresentacionProducto.TipoPresentacion.BANDEJA)
                                : null,
                        s3 != null && s3.getId() != null && s3.getId() != 0
                                ? obtenerPrecioPorPresentacion(s3, PresentacionProducto.TipoPresentacion.BANDEJA)
                                : null
                );
//                return maxPrecio(
//                        obtenerPrecioPorPresentacion(s1, PresentacionProducto.TipoPresentacion.BANDEJA),
//                        s2 != null ? obtenerPrecioPorPresentacion(s2, PresentacionProducto.TipoPresentacion.BANDEJA) : null,
//                        s3 != null ? obtenerPrecioPorPresentacion(s3, PresentacionProducto.TipoPresentacion.BANDEJA) : null
//                );
        }

        return 0.0;
    }


    private Double obtenerPrecioPorPresentacion(SaborPizza sabor, PresentacionProducto.TipoPresentacion tipo) {
        return sabor.getPrecios().stream()
                .filter(p -> p.getPresentacion().getTipo() == tipo)
                .map(PrecioSaborPresentacion::getPrecio)
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("El sabor " + sabor.getNombre() +
                                " no tiene precio definido para la presentación: " + tipo)
                );
    }


    private Double maxPrecio(Double... precios) {
        return Arrays.stream(precios)
                .filter(Objects::nonNull)
                .max(Double::compare)
                .orElse(0.0);
    }

    private Double calcularPromedioPrecios(Double... precios) {
        double suma = 0.0;
        int count = 0;

        for (Double p : precios) {
            if (p != null) {
                suma += p;
                count++;
            }
        }

        return count == 0 ? 0.0 : (suma / count);
    }
}



