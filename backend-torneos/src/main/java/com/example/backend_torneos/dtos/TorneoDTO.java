package com.example.backend_torneos.dtos;

import com.example.backend_torneos.entities.EstadoTorneo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Set;

@Data
public class TorneoDTO {
    private Long id;
    private String nombre;
    private Double prizePool;
    private String fechaInicio;
    private EstadoTorneo estado;
    @JsonProperty("isTrending")
    private boolean isTrending;
    @JsonProperty("isGold")
    private boolean isGold;
    private boolean esPresencial;
    private String pais;
    private String ciudad;
    private Double latitud;
    private Double longitud;
    private Set<UsuarioDTO> organizadores;
    private Set<UsuarioDTO> participantes;

    private JuegoDTO juego;
    private boolean esPorEquipos;
    private Set<EquipoDTO> equiposParticipantes;
}
