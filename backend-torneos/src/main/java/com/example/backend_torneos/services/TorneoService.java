package com.example.backend_torneos.services;

import com.example.backend_torneos.dtos.TorneoCreateDTO;
import com.example.backend_torneos.dtos.TorneoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TorneoService {
    Page<TorneoDTO> getAllTorneos(Long juegoId, Boolean esPorEquipos, Boolean esPresencial, String estado, String nombre, Pageable pageable);
    TorneoDTO getTorneoById(Long id);
    TorneoDTO crearTorneo(TorneoCreateDTO dto, String organizadorEmail);
    TorneoDTO inscribirse(Long torneoId, String email);
    TorneoDTO desinscribirse(Long torneoId, String email);
}
