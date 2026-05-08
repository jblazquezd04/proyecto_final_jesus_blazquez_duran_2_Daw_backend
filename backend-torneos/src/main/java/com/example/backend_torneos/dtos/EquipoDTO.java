package com.example.backend_torneos.dtos;

import com.example.backend_torneos.entities.EstadoEquipo;
import lombok.Data;
import java.util.Set;

@Data
public class EquipoDTO {
    private Long id;
    private String nombre;
    private String logoUrl;
    private EstadoEquipo estado;
    private Long torneoId;
    private String torneoNombre;
    private UsuarioDTO capitan;
    private Set<UsuarioDTO> miembros;
}
