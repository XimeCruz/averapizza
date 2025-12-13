package com.xime.averapizza.security.controller;

import com.xime.averapizza.dto.ProductoCompletoDTO;
import com.xime.averapizza.model.Rol;
import com.xime.averapizza.model.Usuario;
import com.xime.averapizza.repository.RolRepository;
import com.xime.averapizza.repository.UsuarioRepository;
import com.xime.averapizza.security.jwt.JwtService;
import com.xime.averapizza.security.model.AuthRequest;
import com.xime.averapizza.security.model.AuthResponse;
import com.xime.averapizza.service.ProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
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
    private final ProductoService service;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody Usuario usuario,
                           @RequestParam(defaultValue = "CLIENTE") String rolNombre) {
        Rol.RolNombre nombreRol = Rol.RolNombre.valueOf(rolNombre.toUpperCase());
        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));

        Set<Rol> roles = new HashSet<>();
        roles.add(rol);

        usuario.setRoles(roles);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);

        var user = usuario;
        String token = jwtService.generateToken(user.getCorreo());

        return new AuthResponse(token, usuario.getNombre(), user.getCorreo(), rol.getNombre().toString());
        //return "Usuario registrado con rol: " + rolNombre;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
        );

        var user = usuarioRepository.findByCorreo(request.getCorreo());
        String token = jwtService.generateToken(user.getCorreo());

        String rol = String.valueOf(user.getRoles().stream()
                .findFirst()
                .map(Rol::getNombre)
                .orElse(Rol.RolNombre.CLIENTE));

        return new AuthResponse(token, user.getNombre(), user.getCorreo(), rol);
    }


    @GetMapping("/completo")
    public ResponseEntity<List<ProductoCompletoDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodosLosProductos());
    }

    // GET /api/productos/completo/PIZZA
    // GET /api/productos/completo/BEBIDA
    @GetMapping("/completo/{tipo}")
    public ResponseEntity<List<ProductoCompletoDTO>> obtenerPorTipo(
            @PathVariable String tipo) {
        return ResponseEntity.ok(service.obtenerProductosPorTipo(tipo));
    }
}

