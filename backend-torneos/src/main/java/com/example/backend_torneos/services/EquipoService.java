package com.example.backend_torneos.services;

import com.example.backend_torneos.dtos.CrearEquipoDTO;
import com.example.backend_torneos.dtos.EquipoDTO;

import java.util.List;

public interface EquipoService {
    List<EquipoDTO> getAll();
    EquipoDTO crear(CrearEquipoDTO dto, String creadorEmail);
    EquipoDTO unirse(Long equipoId, String email);
}
