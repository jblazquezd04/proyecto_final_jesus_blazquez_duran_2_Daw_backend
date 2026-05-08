package com.example.backend_torneos.controllers;

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

    @GetMapping("/mis-equipos")
    public ResponseEntity<List<EquipoDTO>> getMisEquipos(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(equipoService.getMisEquipos(userDetails.getUsername()));
    }
}
