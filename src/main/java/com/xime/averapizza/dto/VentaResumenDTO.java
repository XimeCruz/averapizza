package com.xime.averapizza.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VentaResumenDTO {
    private Integer id;
    private LocalDateTime fecha;
    private Double total;
}

