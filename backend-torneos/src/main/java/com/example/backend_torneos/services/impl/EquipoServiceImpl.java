package com.example.backend_torneos.services.impl;

import com.example.backend_torneos.dtos.CrearEquipoDTO;
import com.example.backend_torneos.dtos.EquipoDTO;
import com.example.backend_torneos.dtos.mappers.TorneoMapper;
import com.example.backend_torneos.entities.Equipo;
import com.example.backend_torneos.entities.EstadoEquipo;
import com.example.backend_torneos.entities.Torneo;
import com.example.backend_torneos.entities.Usuario;
import com.example.backend_torneos.exceptions.BusinessLogicException;
import com.example.backend_torneos.repositories.EquipoRepository;
import com.example.backend_torneos.repositories.TorneoRepository;
import com.example.backend_torneos.repositories.UsuarioRepository;
import com.example.backend_torneos.services.EquipoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;
    private final TorneoRepository torneoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TorneoMapper torneoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EquipoDTO> getEquiposPorTorneo(Long torneoId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
        return equipoRepository.findByTorneo(torneo).stream()
                .map(torneoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public EquipoDTO crearEquipoParaTorneo(Long torneoId, CrearEquipoDTO dto, String email) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new BusinessLogicException("El nombre del equipo es obligatorio.");
        }

        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        if (!torneo.isEsPorEquipos()) {
            throw new BusinessLogicException("Este torneo no es por equipos.");
        }

        Usuario capitan = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (equipoRepository.findByTorneoAndMiembrosContaining(torneo, capitan).isPresent()) {
            throw new BusinessLogicException("Ya perteneces a un equipo en este torneo.");
        }

        Equipo equipo = Equipo.builder()
                .nombre(dto.getNombre())
                .logoUrl(dto.getLogoUrl())
                .torneo(torneo)
                .capitan(capitan)
                .estado(EstadoEquipo.PENDIENTE)
                .miembros(new HashSet<>(Set.of(capitan)))
                .build();

        return torneoMapper.toDto(equipoRepository.save(equipo));
    }

    @Override
    @Transactional
    public EquipoDTO unirse(Long torneoId, Long equipoId, String email) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (equipoRepository.findByTorneoAndMiembrosContaining(torneo, usuario).isPresent()) {
            throw new BusinessLogicException("Ya perteneces a un equipo en este torneo.");
        }

        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        if (!equipo.getTorneo().getId().equals(torneoId)) {
            throw new BusinessLogicException("El equipo no pertenece a este torneo.");
        }

        if (equipo.getEstado() == EstadoEquipo.RECHAZADO) {
            throw new BusinessLogicException("No puedes unirte a un equipo rechazado.");
        }

        equipo.getMiembros().add(usuario);
        return torneoMapper.toDto(equipoRepository.save(equipo));
    }

    @Override
    @Transactional
    public EquipoDTO aprobar(Long torneoId, Long equipoId, String organizadorEmail) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        boolean esOrganizador = torneo.getOrganizadores().stream()
                .anyMatch(o -> o.getEmail().equals(organizadorEmail));
        if (!esOrganizador) {
            throw new BusinessLogicException("Solo el organizador puede aprobar equipos.");
        }

        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        equipo.setEstado(EstadoEquipo.APROBADO);
        return torneoMapper.toDto(equipoRepository.save(equipo));
    }

    @Override
    @Transactional
    public EquipoDTO rechazar(Long torneoId, Long equipoId, String organizadorEmail) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        boolean esOrganizador = torneo.getOrganizadores().stream()
                .anyMatch(o -> o.getEmail().equals(organizadorEmail));
        if (!esOrganizador) {
            throw new BusinessLogicException("Solo el organizador puede rechazar equipos.");
        }

        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        equipo.setEstado(EstadoEquipo.RECHAZADO);
        return torneoMapper.toDto(equipoRepository.save(equipo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipoDTO> getMisEquipos(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return equipoRepository.findByMiembrosContaining(usuario).stream()
                .map(torneoMapper::toDto)
                .toList();
    }
}
