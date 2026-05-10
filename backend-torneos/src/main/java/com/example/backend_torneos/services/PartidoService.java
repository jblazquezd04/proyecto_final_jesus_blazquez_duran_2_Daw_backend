package com.example.backend_torneos.services;

import com.example.backend_torneos.dtos.PartidoDTO;

import java.util.List;

public interface PartidoService {
    List<PartidoDTO> generarBracket(Long torneoId, String username);
    List<PartidoDTO> getPartidos(Long torneoId);
    PartidoDTO reportarResultado(Long torneoId, Long partidoId, boolean ganadorEsJ1, String username);
    PartidoDTO resolverDisputa(Long torneoId, Long partidoId, boolean ganadorEsJ1, String username);
}
