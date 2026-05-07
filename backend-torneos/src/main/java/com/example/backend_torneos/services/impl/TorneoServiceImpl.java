package com.example.backend_torneos.services.impl;

import com.example.backend_torneos.dtos.TorneoCreateDTO;
import com.example.backend_torneos.dtos.TorneoDTO;
import com.example.backend_torneos.dtos.mappers.TorneoMapper;
import com.example.backend_torneos.entities.EstadoTorneo;
import com.example.backend_torneos.entities.Juego;
import com.example.backend_torneos.entities.Torneo;
import com.example.backend_torneos.entities.Usuario;
import com.example.backend_torneos.repositories.JuegoRepository;
import com.example.backend_torneos.repositories.TorneoRepository;
import com.example.backend_torneos.repositories.UsuarioRepository;
import com.example.backend_torneos.services.TorneoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TorneoServiceImpl implements TorneoService {

    private final TorneoRepository torneoRepository;
    private final TorneoMapper torneoMapper;
    private final UsuarioRepository usuarioRepository;
    private final JuegoRepository juegoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TorneoDTO> getAllTorneos(Long juegoId, Boolean esPorEquipos, Boolean esPresencial, String estado) {
        return torneoRepository.findAll().stream()
                .filter(t -> juegoId == null || (t.getJuego() != null && t.getJuego().getId().equals(juegoId)))
                .filter(t -> esPorEquipos == null || t.isEsPorEquipos() == esPorEquipos)
                .filter(t -> esPresencial == null || t.isEsPresencial() == esPresencial)
                .filter(t -> estado == null || t.getEstado().name().equalsIgnoreCase(estado))
                .map(torneoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TorneoDTO getTorneoById(Long id) {
        Torneo torneo = torneoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
        return torneoMapper.toDto(torneo);
    }

    @Override
    @Transactional
    public TorneoDTO crearTorneo(TorneoCreateDTO dto, String organizadorEmail) {
        Usuario organizador = usuarioRepository.findByEmail(organizadorEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Juego juego = juegoRepository.findById(dto.getJuegoId())
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        Torneo torneo = Torneo.builder()
                .nombre(dto.getNombre())
                .prizePool(dto.getPrizePool())
                .fechaInicio(dto.getFechaInicio())
                .estado(EstadoTorneo.PENDIENTE)
                .isTrending(false)
                .esPresencial(dto.isEsPresencial())
                .pais(dto.getPais())
                .ciudad(dto.getCiudad())
                .latitud(dto.getLatitud())
                .longitud(dto.getLongitud())
                .juego(juego)
                .esPorEquipos(dto.isEsPorEquipos())
                .build();

        torneo.getOrganizadores().add(organizador);

        organizador.setTorneosRealizados(organizador.getTorneosRealizados() + 1);
        usuarioRepository.save(organizador);

        return torneoMapper.toDto(torneoRepository.save(torneo));
    }

    @Override
    @Transactional
    public TorneoDTO inscribirse(Long torneoId, String email) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        if (torneo.getParticipantes().stream().noneMatch(p -> p.getId().equals(user.getId()))) {
            torneo.getParticipantes().add(user);
            user.setTorneosJugados(user.getTorneosJugados() + 1);
            usuarioRepository.save(user);
        }

        return torneoMapper.toDto(torneoRepository.save(torneo));
    }

    @Override
    @Transactional
    public TorneoDTO desinscribirse(Long torneoId, String email) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        boolean removed = torneo.getParticipantes().removeIf(p -> p.getId().equals(user.getId()));
        if (removed) {
            user.setTorneosJugados(Math.max(0, user.getTorneosJugados() - 1));
            usuarioRepository.save(user);
        }

        return torneoMapper.toDto(torneoRepository.save(torneo));
    }
}
