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
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nombre;
    private String imagenUrl;
    private String desarrolladora;
    private String genero;

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("juego")
    @Builder.Default
    private Set<Torneo> torneos = new HashSet<>();
}
