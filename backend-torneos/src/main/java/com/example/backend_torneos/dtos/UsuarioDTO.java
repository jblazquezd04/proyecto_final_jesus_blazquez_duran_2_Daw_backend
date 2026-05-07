package com.example.backend_torneos.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioDTO {
    private Long id;
    private String username;
    private String email;
    private String biografia;
    private String avatarUrl;
    private LocalDateTime fechaRegistro;

    private String modoActual;
    
    // Estadísticas Organizador
    private Integer torneosRealizados;
    private Double premiosOtorgados;

    // Estadísticas Jugador
    private Integer torneosJugados;
    private Integer torneosGanados;
}
