package com.xime.averapizza.repository;

import com.xime.averapizza.model.Rol;
import com.xime.averapizza.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByCorreo(String correo);

    Long countByRolesNombre(Rol.RolNombre rolNombre);

    Long countByActivoTrueAndRolesNombre(Rol.RolNombre rolNombre);

    Long countByActivoFalseAndRolesNombre(Rol.RolNombre rolNombre);

    List<Usuario> findByRolesNombre(Rol.RolNombre rolNombre);

    List<Usuario> findByActivoTrueAndRolesNombre(Rol.RolNombre rolNombre);

    List<Usuario> findByActivoFalseAndRolesNombre(Rol.RolNombre rolNombre);

    Boolean existsByCorreoAndIdNot(String correo, Long id);

}

