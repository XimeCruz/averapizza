package com.xime.averapizza.exception;

import lombok.Getter;

@Getter
public class StockInsuficienteException extends RuntimeException {

    private final String insumo;
    private final Double stockActual;
    private final Double stockRequerido;

    public StockInsuficienteException(String insumo, Double stockActual, Double stockRequerido) {
        super(String.format("Stock insuficiente del insumo \"%s\". Disponible: %.2f, Requerido: %.2f",
                insumo, stockActual, stockRequerido));
        this.insumo = insumo;
        this.stockActual = stockActual;
        this.stockRequerido = stockRequerido;
    }
}