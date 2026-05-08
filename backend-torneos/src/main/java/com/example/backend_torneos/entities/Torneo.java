package com.example.backend_torneos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nombre;
    private Double prizePool;
    private String fechaInicio;

    // Campos de estado y popularidad
    @Enumerated(EnumType.STRING)
    private EstadoTorneo estado;
    
    private boolean isTrending;

    // Campos de geolocalización
    private boolean esPresencial;
    private String pais;
    private String ciudad;
    private Double latitud;
    private Double longitud;

    // Relaciones principales
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "juego_id")
    @JsonIgnoreProperties("torneos")
    private Juego juego;

    private boolean esPorEquipos;

    @ManyToMany
    @JoinTable(
        name = "torneo_equipo",
        joinColumns = @JoinColumn(name = "torneo_id"),
        inverseJoinColumns = @JoinColumn(name = "equipo_id")
    )
    @JsonIgnoreProperties("torneosParticipados")
    @Builder.Default
    private Set<Equipo> equiposParticipantes = new HashSet<>();

    // Relaciones
    @ManyToMany
    @JoinTable(
        name = "torneo_organizador",
        joinColumns = @JoinColumn(name = "torneo_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnoreProperties({"torneosOrganizados", "torneosParticipados"})
    @Builder.Default
    private Set<Usuario> organizadores = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "torneo_participante",
        joinColumns = @JoinColumn(name = "torneo_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnoreProperties({"torneosOrganizados", "torneosParticipados"})
    @Builder.Default
    private Set<Usuario> participantes = new HashSet<>();

    // Método calculado para saber si es Gold (PrizePool >= 10000)
    // Usamos Transient para que no se guarde en la base de datos, 
    // pero el getter hará que se incluya en el JSON de respuesta.
    @Transient
    public boolean isGold() {
        return this.prizePool != null && this.prizePool >= 10000;
    }
}
