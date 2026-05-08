package com.example.backend_torneos.controllers;

import com.example.backend_torneos.dtos.CrearEquipoDTO;
import com.example.backend_torneos.dtos.EquipoDTO;
import com.example.backend_torneos.services.EquipoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoService equipoService;

    @GetMapping
    public List<EquipoDTO> getAll() {
        return equipoService.getAll();
    }

    @PostMapping
    public ResponseEntity<EquipoDTO> crear(
            @RequestBody CrearEquipoDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(equipoService.crear(dto, userDetails.getUsername()));
    }

    @PostMapping("/{id}/unirse")
    public ResponseEntity<EquipoDTO> unirse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(equipoService.unirse(id, userDetails.getUsername()));
    }
}
