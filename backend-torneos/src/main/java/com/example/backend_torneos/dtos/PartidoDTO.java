package com.example.backend_torneos.dtos;

import com.example.backend_torneos.entities.EstadoPartido;
import com.example.backend_torneos.entities.ReporteResultado;
import lombok.Data;

@Data
public class PartidoDTO {
    private Long id;
    private int ronda;
    private int posicion;

    private Long idJ1;
    private String nombreJ1;

    private Long idJ2;
    private String nombreJ2;

    private ReporteResultado reporteJ1;
    private ReporteResultado reporteJ2;

    private Long idGanador;
    private String nombreGanador;

    private EstadoPartido estado;
}
