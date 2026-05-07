package com.example.backend_torneos.controllers;

import com.example.backend_torneos.dtos.TorneoCreateDTO;
import com.example.backend_torneos.dtos.TorneoDTO;
import com.example.backend_torneos.services.TorneoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/torneos")
@RequiredArgsConstructor
public class TorneoController {

    private final TorneoService torneoService;

    @GetMapping("/{id}")
    public ResponseEntity<TorneoDTO> getTorneoById(@PathVariable Long id) {
        return ResponseEntity.ok(torneoService.getTorneoById(id));
    }

    @GetMapping
    public List<TorneoDTO> getTorneos(
            @RequestParam(required = false) Long juegoId,
            @RequestParam(required = false) Boolean esPorEquipos,
            @RequestParam(required = false) Boolean esPresencial,
            @RequestParam(required = false) String estado) {
        return torneoService.getAllTorneos(juegoId, esPorEquipos, esPresencial, estado);
    }

    @PostMapping
    public ResponseEntity<TorneoDTO> crearTorneo(
            @RequestBody TorneoCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(torneoService.crearTorneo(dto, userDetails.getUsername()));
    }

    @PostMapping("/{id}/inscribirse")
    public ResponseEntity<TorneoDTO> inscribirse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(torneoService.inscribirse(id, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}/inscribirse")
    public ResponseEntity<TorneoDTO> desinscribirse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(torneoService.desinscribirse(id, userDetails.getUsername()));
    }
}
