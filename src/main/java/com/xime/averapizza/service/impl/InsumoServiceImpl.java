package com.xime.averapizza.service.impl;

import com.xime.averapizza.dto.InsumoCalculadoDTO;
import com.xime.averapizza.model.Insumo;
import com.xime.averapizza.model.MovimientoInventario;
import com.xime.averapizza.repository.InsumoRepository;
import com.xime.averapizza.repository.MovimientoInventarioRepository;
import com.xime.averapizza.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InsumoServiceImpl implements InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @Override
    public void descontarStock(Long insumoId, Double cantidad, String referencia, Integer usuarioId) {
        Insumo insumo = insumoRepository.findById(insumoId)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado: " + insumoId));

        // Validar stock suficiente
        if (!insumo.tieneStockSuficiente(cantidad)) {
            throw new RuntimeException(
                    String.format("Stock insuficiente de %s. Necesario: %.4f %s, Disponible: %.4f %s",
                            insumo.getNombre(),
                            cantidad,
                            insumo.getUnidadMedida(),
                            insumo.getStockActual(),
                            insumo.getUnidadMedida()
                    )
            );
        }

        // Descontar stock
        insumo.descontar(cantidad);
        insumoRepository.save(insumo);

        // Registrar movimiento
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setInsumo(insumo);
        movimiento.setTipoMovimiento("SALIDA");
        movimiento.setCantidad(cantidad);
        movimiento.setReferencia(referencia);
        movimiento.setUsuarioId(usuarioId);
        movimiento.setFechaHora(LocalDateTime.now());

        movimientoRepository.save(movimiento);
    }

    @Override
    public void verificarStockDisponible(List<InsumoCalculadoDTO> insumos) {
        List<String> faltantes = new ArrayList<>();

        for (InsumoCalculadoDTO dto : insumos) {
            if (dto.getStockActual() < dto.getCantidadNecesaria()) {
                faltantes.add(
                        String.format("%s (Necesario: %.4f %s, Disponible: %.4f %s)",
                                dto.getNombreInsumo(),
                                dto.getCantidadNecesaria(),
                                dto.getUnidadMedida(),
                                dto.getStockActual(),
                                dto.getUnidadMedida()
                        )
                );
            }
        }

        if (!faltantes.isEmpty()) {
            throw new RuntimeException(
                    "Stock insuficiente para:\n" + String.join("\n", faltantes)
            );
        }
    }
}
