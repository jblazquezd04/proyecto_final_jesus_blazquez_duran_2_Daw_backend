package com.example.backend_torneos.services.impl;

import com.example.backend_torneos.dtos.CrearEquipoDTO;
import com.example.backend_torneos.dtos.EquipoDTO;
import com.example.backend_torneos.dtos.mappers.TorneoMapper;
import com.example.backend_torneos.entities.Equipo;
import com.example.backend_torneos.entities.Usuario;
import com.example.backend_torneos.exceptions.BusinessLogicException;
import com.example.backend_torneos.repositories.EquipoRepository;
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
    private final UsuarioRepository usuarioRepository;
    private final TorneoMapper torneoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EquipoDTO> getAll() {
        return equipoRepository.findAll().stream()
                .map(torneoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public EquipoDTO crear(CrearEquipoDTO dto, String creadorEmail) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new BusinessLogicException("El nombre del equipo es obligatorio.");
        }
        if (equipoRepository.findByNombre(dto.getNombre()).isPresent()) {
            throw new BusinessLogicException("Ya existe un equipo con ese nombre.");
        }

        Usuario creador = usuarioRepository.findByEmail(creadorEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Equipo equipo = Equipo.builder()
                .nombre(dto.getNombre())
                .logoUrl(dto.getLogoUrl())
                .miembros(new HashSet<>(Set.of(creador)))
                .build();

        return torneoMapper.toDto(equipoRepository.save(equipo));
    }

    @Override
    @Transactional
    public EquipoDTO unirse(Long equipoId, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        boolean yaEsMiembro = equipo.getMiembros().stream()
                .anyMatch(m -> m.getId().equals(usuario.getId()));
        if (yaEsMiembro) {
            throw new BusinessLogicException("Ya eres miembro de este equipo.");
        }

        equipo.getMiembros().add(usuario);
        return torneoMapper.toDto(equipoRepository.save(equipo));
    }
}
