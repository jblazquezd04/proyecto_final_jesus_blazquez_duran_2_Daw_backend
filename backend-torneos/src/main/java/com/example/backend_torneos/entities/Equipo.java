package com.example.backend_torneos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nombre;
    private String logoUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoEquipo estado = EstadoEquipo.PENDIENTE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "torneo_id")
    @JsonIgnoreProperties({"equiposParticipantes", "participantes", "organizadores"})
    private Torneo torneo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "capitan_id")
    @JsonIgnoreProperties({"torneosOrganizados", "torneosParticipados"})
    private Usuario capitan;

    @ManyToMany
    @JoinTable(
        name = "equipo_miembro",
        joinColumns = @JoinColumn(name = "equipo_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnoreProperties({"torneosParticipados", "torneosOrganizados"})
    @Builder.Default
    private Set<Usuario> miembros = new HashSet<>();
}
