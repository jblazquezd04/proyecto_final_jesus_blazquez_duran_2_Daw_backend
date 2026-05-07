package com.example.backend_torneos.controllers;

import com.example.backend_torneos.dtos.AuthResponse;
import com.example.backend_torneos.dtos.CambiarModoRequest;
import com.example.backend_torneos.dtos.ChangePasswordRequest;
import com.example.backend_torneos.dtos.UpdatePerfilRequest;
import com.example.backend_torneos.dtos.UsuarioDTO;
import com.example.backend_torneos.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(usuarioService.getMe(userDetails.getUsername()));
    }

    @PutMapping("/me/modo")
    public ResponseEntity<AuthResponse> cambiarModo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CambiarModoRequest request) {
        return ResponseEntity.ok(usuarioService.cambiarModo(userDetails.getUsername(), request));
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioDTO> updatePerfil(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdatePerfilRequest request) {
        return ResponseEntity.ok(usuarioService.updatePerfil(userDetails.getUsername(), request));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> cambiarPassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChangePasswordRequest request) {
        usuarioService.cambiarPassword(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> eliminarCuenta(@AuthenticationPrincipal UserDetails userDetails) {
        usuarioService.eliminarCuenta(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
