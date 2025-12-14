package com.xime.averapizza.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        // Extraer el mensaje de error limpio
        String mensaje = ex.getMessage();

        // Si el error viene de PostgreSQL, extraer solo la parte relevante
        if (mensaje != null && mensaje.contains("ERROR:")) {
            int errorIndex = mensaje.indexOf("ERROR:");
            int whereIndex = mensaje.indexOf("Where:");

            if (errorIndex != -1) {
                if (whereIndex != -1) {
                    mensaje = mensaje.substring(errorIndex + 7, whereIndex).trim();
                } else {
                    mensaje = mensaje.substring(errorIndex + 7).trim();
                }
            }
        }

        errorResponse.put("success", false);
        errorResponse.put("mensaje", mensaje);
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleStockInsuficienteException(StockInsuficienteException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("success", false);
        errorResponse.put("mensaje", ex.getMessage());
        errorResponse.put("tipo", "STOCK_INSUFICIENTE");
        errorResponse.put("insumo", ex.getInsumo());
        errorResponse.put("stockActual", ex.getStockActual());
        errorResponse.put("stockRequerido", ex.getStockRequerido());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("success", false);
        errorResponse.put("mensaje", ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("success", false);
        errorResponse.put("mensaje", "Error interno del servidor: " + ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());

        // Log del error completo para debugging
        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
