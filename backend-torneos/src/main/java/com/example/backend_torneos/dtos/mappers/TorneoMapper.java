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

import java.util.List;

@Mapper(componentModel = "spring")
public interface TorneoMapper {

    TorneoDTO toDto(Torneo torneo);

    Torneo toEntity(TorneoDTO torneoDTO);

    UsuarioDTO toDto(Usuario usuario);
    
    Usuario toEntity(UsuarioDTO usuarioDTO);
    
    List<TorneoDTO> toDtoList(List<Torneo> torneos);

    JuegoDTO toDto(Juego juego);
    Juego toEntity(JuegoDTO juegoDTO);
    
    EquipoDTO toDto(Equipo equipo);
    Equipo toEntity(EquipoDTO equipoDTO);
}
