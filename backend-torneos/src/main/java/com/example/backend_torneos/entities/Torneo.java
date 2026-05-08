package com.example.backend_torneos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Enumerated(EnumType.STRING)
    private EstadoTorneo estado;

    private boolean isTrending;

    private boolean esPresencial;
    private String pais;
    private String ciudad;
    private Double latitud;
    private Double longitud;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "juego_id")
    @JsonIgnoreProperties("torneos")
    private Juego juego;

    private boolean esPorEquipos;

    // Equipos ligados a este torneo (OneToMany — cada equipo pertenece a un torneo)
    @OneToMany(mappedBy = "torneo", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"torneo", "miembros", "capitan"})
    @Builder.Default
    private List<Equipo> equiposParticipantes = new ArrayList<>();

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

    @Transient
    public boolean isGold() {
        return this.prizePool != null && this.prizePool >= 10000;
    }
}
