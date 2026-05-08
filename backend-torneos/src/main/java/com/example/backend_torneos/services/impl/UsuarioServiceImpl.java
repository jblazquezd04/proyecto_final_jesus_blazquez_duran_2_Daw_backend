package com.example.backend_torneos.services.impl;

import com.example.backend_torneos.dtos.AuthResponse;
import com.example.backend_torneos.dtos.CambiarModoRequest;
import com.example.backend_torneos.dtos.ChangePasswordRequest;
import com.example.backend_torneos.dtos.UpdatePerfilRequest;
import com.example.backend_torneos.dtos.UsuarioDTO;
import com.example.backend_torneos.dtos.mappers.TorneoMapper;
import com.example.backend_torneos.entities.Equipo;
import com.example.backend_torneos.entities.ModoUsuario;
import com.example.backend_torneos.entities.Torneo;
import com.example.backend_torneos.entities.Usuario;
import com.example.backend_torneos.exceptions.BusinessLogicException;
import com.example.backend_torneos.repositories.EquipoRepository;
import com.example.backend_torneos.repositories.TorneoRepository;
import com.example.backend_torneos.repositories.UsuarioRepository;
import com.example.backend_torneos.security.JwtUtil;
import com.example.backend_torneos.services.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TorneoRepository torneoRepository;
    private final EquipoRepository equipoRepository;
    private final TorneoMapper torneoMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO getMe(String email) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return torneoMapper.toDto(user);
    }

    @Override
    public AuthResponse cambiarModo(String email, CambiarModoRequest request) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ModoUsuario nuevoModo = ModoUsuario.valueOf(request.getModoActual());
        user.setModoActual(nuevoModo);
        usuarioRepository.save(user);

        String token = jwtUtil.generateToken(
                new User(user.getEmail(), user.getPassword(), Collections.emptyList()));

        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .modoActual(user.getModoActual().name())
                .build();
    }

    @Override
    public UsuarioDTO updatePerfil(String email, UpdatePerfilRequest request) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getBiografia() != null) user.setBiografia(request.getBiografia());
        if (request.getTelefono() != null) user.setTelefono(request.getTelefono());
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername());
        }

        usuarioRepository.save(user);
        return torneoMapper.toDto(user);
    }

    @Override
    public void cambiarPassword(String email, ChangePasswordRequest request) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPasswordActual(), user.getPassword())) {
            throw new BusinessLogicException("La contraseña actual no es correcta.");
        }

        user.setPassword(passwordEncoder.encode(request.getPasswordNueva()));
        usuarioRepository.save(user);
    }

    @Override
    @Transactional
    public void eliminarCuenta(String email) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Torneo> torneos = torneoRepository.findAll();
        for (Torneo torneo : torneos) {
            boolean changed = torneo.getOrganizadores().removeIf(u -> u.getId().equals(user.getId()));
            changed |= torneo.getParticipantes().removeIf(u -> u.getId().equals(user.getId()));
            if (changed) torneoRepository.save(torneo);
        }

        List<Equipo> equipos = equipoRepository.findAll();
        for (Equipo equipo : equipos) {
            if (equipo.getMiembros().removeIf(u -> u.getId().equals(user.getId()))) {
                equipoRepository.save(equipo);
            }
        }

        usuarioRepository.delete(user);
    }
}
