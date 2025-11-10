package com.xime.averapizza.security.config;

import com.xime.averapizza.model.Usuario;
import com.xime.averapizza.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + correo);
        }

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPassword())
                .disabled(!usuario.isActivo())
                .authorities(usuario.getRoles().stream()
                        .map(rol -> "ROLE_" + rol.getNombre())
                        .toArray(String[]::new))
                .build();
    }
}
