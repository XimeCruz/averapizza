package com.xime.averapizza.security.controller;

import com.xime.averapizza.model.Rol;
import com.xime.averapizza.model.Usuario;
import com.xime.averapizza.repository.RolRepository;
import com.xime.averapizza.repository.UsuarioRepository;
import com.xime.averapizza.security.jwt.JwtService;
import com.xime.averapizza.security.model.AuthRequest;
import com.xime.averapizza.security.model.AuthResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@Tag(name = "Registro y autentificación", description = "Registro y login de usuarios")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    @PostMapping("/register")
    public String register(@RequestBody Usuario usuario,
                           @RequestParam(defaultValue = "CLIENTE") String rolNombre) {
        Rol.RolNombre nombreRol = Rol.RolNombre.valueOf(rolNombre.toUpperCase());
        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));

        Set<Rol> roles = new HashSet<>();
        roles.add(rol);

        usuario.setRoles(roles);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);

        return "Usuario registrado con rol: " + rolNombre;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
        );

        var user = usuarioRepository.findByCorreo(request.getCorreo());
        String token = jwtService.generateToken(user.getCorreo());
        return new AuthResponse(token);
    }
}

