package com.example.backend_torneos.dtos.mappers;

import com.example.backend_torneos.dtos.TorneoDTO;
import com.example.backend_torneos.dtos.UsuarioDTO;
import com.example.backend_torneos.entities.Torneo;
import com.example.backend_torneos.entities.Usuario;
import com.example.backend_torneos.dtos.JuegoDTO;
import com.example.backend_torneos.dtos.EquipoDTO;
import com.example.backend_torneos.entities.Juego;
import com.example.backend_torneos.entities.Equipo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TorneoMapper {

    TorneoDTO toDto(Torneo torneo);

    @Mapping(target = "equiposParticipantes", ignore = true)
    @Mapping(target = "isTrending", ignore = true)
    Torneo toEntity(TorneoDTO torneoDTO);

    UsuarioDTO toDto(Usuario usuario);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "telefono", ignore = true)
    @Mapping(target = "torneosOrganizados", ignore = true)
    @Mapping(target = "torneosParticipados", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);

    List<TorneoDTO> toDtoList(List<Torneo> torneos);

    JuegoDTO toDto(Juego juego);
    Juego toEntity(JuegoDTO juegoDTO);

    @Mapping(source = "torneo.id", target = "torneoId")
    @Mapping(source = "torneo.nombre", target = "torneoNombre")
    EquipoDTO toDto(Equipo equipo);

    @Mapping(target = "torneo", ignore = true)
    @Mapping(target = "capitan", ignore = true)
    Equipo toEntity(EquipoDTO equipoDTO);
}
