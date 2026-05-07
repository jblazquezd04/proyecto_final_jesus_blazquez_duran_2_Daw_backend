package com.example.backend_torneos.dtos;

import lombok.Data;
import java.util.Set;

@Data
public class EquipoDTO {
    private Long id;
    private String nombre;
    private String logoUrl;
    private Set<UsuarioDTO> miembros;
}
