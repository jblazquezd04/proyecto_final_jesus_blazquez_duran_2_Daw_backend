package com.example.backend_torneos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String logoUrl;

    @ManyToMany
    @JoinTable(
        name = "equipo_miembro",
        joinColumns = @JoinColumn(name = "equipo_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnoreProperties({"torneosParticipados", "torneosOrganizados"})
    @Builder.Default
    private Set<Usuario> miembros = new HashSet<>();
    
    @ManyToMany(mappedBy = "equiposParticipantes")
    @JsonIgnoreProperties({"participantes", "equiposParticipantes", "organizadores", "juego"})
    @Builder.Default
    private Set<Torneo> torneosParticipados = new HashSet<>();
}
