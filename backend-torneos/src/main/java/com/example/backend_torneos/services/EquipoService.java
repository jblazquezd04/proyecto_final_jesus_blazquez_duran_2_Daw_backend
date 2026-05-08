package com.example.backend_torneos.services;

import com.example.backend_torneos.dtos.CrearEquipoDTO;
import com.example.backend_torneos.dtos.EquipoDTO;

import java.util.List;

public interface EquipoService {
    List<EquipoDTO> getEquiposPorTorneo(Long torneoId);
    EquipoDTO crearEquipoParaTorneo(Long torneoId, CrearEquipoDTO dto, String email);
    EquipoDTO unirse(Long torneoId, Long equipoId, String email);
    EquipoDTO aprobar(Long torneoId, Long equipoId, String organizadorEmail);
    EquipoDTO rechazar(Long torneoId, Long equipoId, String organizadorEmail);
    List<EquipoDTO> getMisEquipos(String email);
}
