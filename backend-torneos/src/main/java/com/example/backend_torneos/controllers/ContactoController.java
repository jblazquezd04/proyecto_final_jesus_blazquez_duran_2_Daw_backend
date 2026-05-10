package com.example.backend_torneos.controllers;

import com.example.backend_torneos.dtos.ContactoRequest;
import com.example.backend_torneos.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contacto")
@RequiredArgsConstructor
public class ContactoController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Map<String, String>> enviar(@RequestBody ContactoRequest request) {
        emailService.sendContactAck(request.getEmail(), request.getNombre());
        emailService.forwardContact(request.getNombre(), request.getEmail(), request.getMensaje());
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
