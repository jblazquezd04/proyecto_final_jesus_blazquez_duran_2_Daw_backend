package com.example.backend_torneos.controllers;

import com.example.backend_torneos.dtos.JuegoDTO;
import com.example.backend_torneos.dtos.mappers.TorneoMapper;
import com.example.backend_torneos.repositories.JuegoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juegos")
@RequiredArgsConstructor
public class JuegoController {

    private final JuegoRepository juegoRepository;
    private final TorneoMapper torneoMapper;

    @GetMapping
    public List<JuegoDTO> getJuegos() {
        return juegoRepository.findAll().stream()
                .map(torneoMapper::toDto)
                .toList();
    }
}
