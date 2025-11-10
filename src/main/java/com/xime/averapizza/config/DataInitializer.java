package com.xime.averapizza.config;

import com.xime.averapizza.model.Rol;
import com.xime.averapizza.repository.RolRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RolRepository rolRepository;

    @PostConstruct
    public void init() {
        if (rolRepository.findAll().isEmpty()) {
            rolRepository.save(new Rol(null, Rol.RolNombre.ADMIN));
            rolRepository.save(new Rol(null, Rol.RolNombre.CAJERO));
            rolRepository.save(new Rol(null, Rol.RolNombre.CLIENTE));
            System.out.println("✅ Roles inicializados correctamente");
        }
    }
}
