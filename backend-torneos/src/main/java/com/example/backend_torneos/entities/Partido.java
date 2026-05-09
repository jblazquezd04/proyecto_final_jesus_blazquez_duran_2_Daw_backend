package com.example.backend_torneos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    @JsonIgnoreProperties({"equiposParticipantes", "participantes", "organizadores", "juego"})
    private Torneo torneo;

    private int ronda;
    private int posicion;

    // ── Participantes individuales ────────────────────────────────
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jugador1_id")
    @JsonIgnoreProperties({"torneosOrganizados", "torneosParticipados"})
    private Usuario jugador1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jugador2_id")
    @JsonIgnoreProperties({"torneosOrganizados", "torneosParticipados"})
    private Usuario jugador2;

    // ── Participantes por equipo ──────────────────────────────────
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipo1_id")
    @JsonIgnoreProperties({"torneo", "miembros", "capitan"})
    private Equipo equipo1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipo2_id")
    @JsonIgnoreProperties({"torneo", "miembros", "capitan"})
    private Equipo equipo2;

    // ── Reportes de resultado ─────────────────────────────────────
    @Enumerated(EnumType.STRING)
    private ReporteResultado reporteJ1;

    @Enumerated(EnumType.STRING)
    private ReporteResultado reporteJ2;

    // ── Ganador resuelto ─────────────────────────────────────────
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ganador_usuario_id")
    @JsonIgnoreProperties({"torneosOrganizados", "torneosParticipados"})
    private Usuario ganadorUsuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ganador_equipo_id")
    @JsonIgnoreProperties({"torneo", "miembros", "capitan"})
    private Equipo ganadorEquipo;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoPartido estado = EstadoPartido.PENDIENTE;
}
