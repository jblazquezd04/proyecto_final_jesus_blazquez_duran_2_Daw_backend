package com.example.backend_torneos.services;

import com.example.backend_torneos.dtos.AuthRequest;
import com.example.backend_torneos.dtos.AuthResponse;
import com.example.backend_torneos.dtos.RegisterRequest;
import com.example.backend_torneos.entities.ModoUsuario;
import com.example.backend_torneos.entities.Usuario;
import com.example.backend_torneos.repositories.UsuarioRepository;
import com.example.backend_torneos.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        Usuario user = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .modoActual(ModoUsuario.JUGADOR)
                .build();

        usuarioRepository.save(user);

        String jwtToken = jwtUtil.generateToken(new User(user.getEmail(), user.getPassword(), Collections.emptyList()));

        return AuthResponse.builder()
                .id(user.getId())
                .token(jwtToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .modoActual(user.getModoActual().name())
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Usuario user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String jwtToken = jwtUtil.generateToken(new User(user.getEmail(), user.getPassword(), Collections.emptyList()));

        return AuthResponse.builder()
                .id(user.getId())
                .token(jwtToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .modoActual(user.getModoActual().name())
                .build();
    }
}
