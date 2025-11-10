package com.xime.averapizza.security.controller;

import com.xime.averapizza.model.Usuario;
import com.xime.averapizza.repository.UsuarioRepository;
import com.xime.averapizza.security.jwt.JwtService;
import com.xime.averapizza.security.model.AuthRequest;
import com.xime.averapizza.security.model.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return "Usuario registrado exitosamente.";
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

