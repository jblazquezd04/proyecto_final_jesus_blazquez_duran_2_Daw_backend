package com.example.backend_torneos.services;

import com.example.backend_torneos.dtos.TorneoCreateDTO;
import com.example.backend_torneos.dtos.TorneoDTO;

import java.util.List;

public interface TorneoService {
    List<TorneoDTO> getAllTorneos(Long juegoId, Boolean esPorEquipos, Boolean esPresencial, String estado);
    TorneoDTO getTorneoById(Long id);
    TorneoDTO crearTorneo(TorneoCreateDTO dto, String organizadorEmail);
    TorneoDTO inscribirse(Long torneoId, String email);
    TorneoDTO desinscribirse(Long torneoId, String email);
}
