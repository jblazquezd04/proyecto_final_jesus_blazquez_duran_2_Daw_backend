package com.example.backend_torneos.dtos;

import lombok.Data;

@Data
public class TorneoCreateDTO {
    private String nombre;
    private Double prizePool;
    private String fechaInicio;
    private Long juegoId;
    private boolean esPorEquipos;
    private boolean esPresencial;
    private String pais;
    private String ciudad;
    private Double latitud;
    private Double longitud;
}
