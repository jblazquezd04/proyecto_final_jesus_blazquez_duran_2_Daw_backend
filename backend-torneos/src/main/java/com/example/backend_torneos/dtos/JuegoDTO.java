package com.example.backend_torneos.dtos;

import lombok.Data;

@Data
public class JuegoDTO {
    private Long id;
    private String nombre;
    private String imagenUrl;
    private String desarrolladora;
    private String genero;
}
