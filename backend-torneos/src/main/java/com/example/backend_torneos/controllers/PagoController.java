package com.example.backend_torneos.controllers;

import com.example.backend_torneos.dtos.PagoConfirmacionRequest;
import com.example.backend_torneos.entities.Usuario;
import com.example.backend_torneos.repositories.UsuarioRepository;
import com.example.backend_torneos.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/confirmar")
    public ResponseEntity<Map<String, String>> confirmar(
            @RequestBody PagoConfirmacionRequest request,
            @AuthenticationPrincipal UserDetails principal) {

        Usuario usuario = usuarioRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        emailService.sendPaymentConfirmation(
                usuario.getEmail(),
                usuario.getUsername(),
                request.getOrderId(),
                request.getTotal()
        );

        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
