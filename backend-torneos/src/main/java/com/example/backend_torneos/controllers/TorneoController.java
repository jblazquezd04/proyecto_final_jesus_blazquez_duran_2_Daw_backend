package com.example.backend_torneos.controllers;

import com.example.backend_torneos.dtos.CrearEquipoDTO;
import com.example.backend_torneos.dtos.EquipoDTO;
import com.example.backend_torneos.dtos.TorneoCreateDTO;
import com.example.backend_torneos.dtos.TorneoDTO;
import com.example.backend_torneos.services.EquipoService;
import com.example.backend_torneos.services.TorneoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/torneos")
@RequiredArgsConstructor
public class TorneoController {

    private final TorneoService torneoService;
    private final EquipoService equipoService;

    @GetMapping("/{id}")
    public ResponseEntity<TorneoDTO> getTorneoById(@PathVariable Long id) {
        return ResponseEntity.ok(torneoService.getTorneoById(id));
    }

    @GetMapping
    public Page<TorneoDTO> getTorneos(
            @RequestParam(required = false) Long juegoId,
            @RequestParam(required = false) Boolean esPorEquipos,
            @RequestParam(required = false) Boolean esPresencial,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return torneoService.getAllTorneos(juegoId, esPorEquipos, esPresencial, estado, nombre, PageRequest.of(page, size));
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

    // ── Endpoints de Equipos ────────────────────────────────────

    @GetMapping("/{id}/equipos")
    public List<EquipoDTO> getEquipos(@PathVariable Long id) {
        return equipoService.getEquiposPorTorneo(id);
    }

    @PostMapping("/{id}/equipos")
    public ResponseEntity<EquipoDTO> crearEquipo(
            @PathVariable Long id,
            @RequestBody CrearEquipoDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(equipoService.crearEquipoParaTorneo(id, dto, userDetails.getUsername()));
    }

    @PostMapping("/{id}/equipos/{equipoId}/unirse")
    public ResponseEntity<EquipoDTO> unirseEquipo(
            @PathVariable Long id,
            @PathVariable Long equipoId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(equipoService.unirse(id, equipoId, userDetails.getUsername()));
    }

    @PutMapping("/{id}/equipos/{equipoId}/aprobar")
    public ResponseEntity<EquipoDTO> aprobarEquipo(
            @PathVariable Long id,
            @PathVariable Long equipoId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(equipoService.aprobar(id, equipoId, userDetails.getUsername()));
    }

    @PutMapping("/{id}/equipos/{equipoId}/rechazar")
    public ResponseEntity<EquipoDTO> rechazarEquipo(
            @PathVariable Long id,
            @PathVariable Long equipoId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(equipoService.rechazar(id, equipoId, userDetails.getUsername()));
    }
}
