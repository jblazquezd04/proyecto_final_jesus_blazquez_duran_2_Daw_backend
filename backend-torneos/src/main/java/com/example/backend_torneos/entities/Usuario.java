package com.example.backend_torneos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String username;
    private String email;
    private String password;
    private String telefono;
    private String biografia;
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ModoUsuario modoActual = ModoUsuario.JUGADOR;

    // Estadísticas Organizador
    @Builder.Default
    private Integer torneosRealizados = 0;
    
    @Builder.Default
    private Double premiosOtorgados = 0.0;

    // Estadísticas Jugador
    @Builder.Default
    private Integer torneosJugados = 0;
    
    @Builder.Default
    private Integer torneosGanados = 0;
    
    @Builder.Default
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @ManyToMany(mappedBy = "organizadores")
    @JsonIgnoreProperties({"organizadores", "participantes"})
    @Builder.Default
    private Set<Torneo> torneosOrganizados = new HashSet<>();

    @ManyToMany(mappedBy = "participantes")
    @JsonIgnoreProperties({"organizadores", "participantes"})
    @Builder.Default
    private Set<Torneo> torneosParticipados = new HashSet<>();
}
