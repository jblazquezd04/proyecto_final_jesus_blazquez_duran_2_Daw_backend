package com.example.backend_torneos.services.impl;

import com.example.backend_torneos.dtos.PartidoDTO;
import com.example.backend_torneos.entities.*;
import com.example.backend_torneos.repositories.*;
import com.example.backend_torneos.services.EmailService;
import com.example.backend_torneos.services.PartidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidoServiceImpl implements PartidoService {

    private final PartidoRepository partidoRepository;
    private final TorneoRepository torneoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EquipoRepository equipoRepository;
    private final EmailService emailService;

    // ── Generar bracket ───────────────────────────────────────────

    @Override
    @Transactional
    public List<PartidoDTO> generarBracket(Long torneoId, String username) {
        Torneo torneo = findTorneo(torneoId);
        verificarOrganizador(torneo, username);

        partidoRepository.deleteByTorneo(torneo);

        if (torneo.isEsPorEquipos()) {
            return generarBracketEquipos(torneo);
        } else {
            return generarBracketIndividual(torneo);
        }
    }

    private List<PartidoDTO> generarBracketIndividual(Torneo torneo) {
        List<Usuario> participantes = new ArrayList<>(torneo.getParticipantes());
        Collections.shuffle(participantes);

        int n = siguientePotenciaDeDos(participantes.size());
        int numRondas = (int) (Math.log(n) / Math.log(2));
        List<Partido> partidos = new ArrayList<>();

        // Ronda 1: rellenar con BYEs si n > participantes
        for (int pos = 0; pos < n / 2; pos++) {
            Usuario j1 = pos * 2 < participantes.size() ? participantes.get(pos * 2) : null;
            Usuario j2 = pos * 2 + 1 < participantes.size() ? participantes.get(pos * 2 + 1) : null;

            Partido p = Partido.builder()
                    .torneo(torneo)
                    .ronda(1)
                    .posicion(pos)
                    .jugador1(j1)
                    .jugador2(j2)
                    .estado(EstadoPartido.PENDIENTE)
                    .build();

            // BYE: un lado vacío → ganador automático
            if (j1 == null && j2 != null) {
                p.setGanadorUsuario(j2);
                p.setEstado(EstadoPartido.CONFIRMADO);
            } else if (j2 == null && j1 != null) {
                p.setGanadorUsuario(j1);
                p.setEstado(EstadoPartido.CONFIRMADO);
            }

            partidos.add(partidoRepository.save(p));
        }

        // Rondas siguientes con participantes nulos (se rellenan al confirmar resultado)
        for (int ronda = 2; ronda <= numRondas; ronda++) {
            int matchesEnRonda = n / (int) Math.pow(2, ronda);
            for (int pos = 0; pos < matchesEnRonda; pos++) {
                Partido p = Partido.builder()
                        .torneo(torneo)
                        .ronda(ronda)
                        .posicion(pos)
                        .estado(EstadoPartido.PENDIENTE)
                        .build();
                partidos.add(partidoRepository.save(p));
            }
        }

        // Propagar ganadores automáticos de BYE a la siguiente ronda
        propagarGanadoresBye(partidos, false);

        return partidos.stream().map(this::toDto).toList();
    }

    private List<PartidoDTO> generarBracketEquipos(Torneo torneo) {
        List<Equipo> equipos = equipoRepository.findByTorneoAndEstado(torneo, EstadoEquipo.APROBADO);
        Collections.shuffle(equipos);

        int n = siguientePotenciaDeDos(equipos.size());
        int numRondas = (int) (Math.log(n) / Math.log(2));
        List<Partido> partidos = new ArrayList<>();

        for (int pos = 0; pos < n / 2; pos++) {
            Equipo e1 = pos * 2 < equipos.size() ? equipos.get(pos * 2) : null;
            Equipo e2 = pos * 2 + 1 < equipos.size() ? equipos.get(pos * 2 + 1) : null;

            Partido p = Partido.builder()
                    .torneo(torneo)
                    .ronda(1)
                    .posicion(pos)
                    .equipo1(e1)
                    .equipo2(e2)
                    .estado(EstadoPartido.PENDIENTE)
                    .build();

            if (e1 == null && e2 != null) {
                p.setGanadorEquipo(e2);
                p.setEstado(EstadoPartido.CONFIRMADO);
            } else if (e2 == null && e1 != null) {
                p.setGanadorEquipo(e1);
                p.setEstado(EstadoPartido.CONFIRMADO);
            }

            partidos.add(partidoRepository.save(p));
        }

        for (int ronda = 2; ronda <= numRondas; ronda++) {
            int matchesEnRonda = n / (int) Math.pow(2, ronda);
            for (int pos = 0; pos < matchesEnRonda; pos++) {
                Partido p = Partido.builder()
                        .torneo(torneo)
                        .ronda(ronda)
                        .posicion(pos)
                        .estado(EstadoPartido.PENDIENTE)
                        .build();
                partidos.add(partidoRepository.save(p));
            }
        }

        propagarGanadoresBye(partidos, true);

        return partidos.stream().map(this::toDto).toList();
    }

    // ── Get partidos ──────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<PartidoDTO> getPartidos(Long torneoId) {
        Torneo torneo = findTorneo(torneoId);
        return partidoRepository.findByTorneoOrderByRondaAscPosicionAsc(torneo)
                .stream().map(this::toDto).toList();
    }

    // ── Reportar resultado ────────────────────────────────────────

    @Override
    @Transactional
    public PartidoDTO reportarResultado(Long torneoId, Long partidoId, boolean ganadorEsJ1, String username) {
        Torneo torneo = findTorneo(torneoId);
        Partido partido = findPartido(partidoId, torneo);

        ReporteResultado reporte = ganadorEsJ1 ? ReporteResultado.J1_GANA : ReporteResultado.J2_GANA;
        boolean esJ1 = esParticipante1(partido, username, torneo.isEsPorEquipos());

        if (esJ1) {
            partido.setReporteJ1(reporte);
        } else {
            partido.setReporteJ2(reporte);
        }

        actualizarEstadoTrasReporte(partido, torneo);
        return toDto(partidoRepository.save(partido));
    }

    // ── Resolver disputa (organizador) ────────────────────────────

    @Override
    @Transactional
    public PartidoDTO resolverDisputa(Long torneoId, Long partidoId, boolean ganadorEsJ1, String username) {
        Torneo torneo = findTorneo(torneoId);
        verificarOrganizador(torneo, username);
        Partido partido = findPartido(partidoId, torneo);

        aplicarGanador(partido, ganadorEsJ1, torneo.isEsPorEquipos());
        partido.setEstado(EstadoPartido.CONFIRMADO);
        Partido guardado = partidoRepository.save(partido);
        avanzarGanador(guardado, torneo);
        return toDto(guardado);
    }

    // ── Helpers ───────────────────────────────────────────────────

    private void actualizarEstadoTrasReporte(Partido p, Torneo torneo) {
        boolean ambosReportaron = p.getReporteJ1() != null && p.getReporteJ2() != null;
        if (!ambosReportaron) {
            p.setEstado(EstadoPartido.ESPERANDO_CONFIRMACION);
            return;
        }
        if (p.getReporteJ1() == p.getReporteJ2()) {
            // Coinciden
            aplicarGanador(p, p.getReporteJ1() == ReporteResultado.J1_GANA, torneo.isEsPorEquipos());
            p.setEstado(EstadoPartido.CONFIRMADO);
            Partido guardado = partidoRepository.save(p);
            avanzarGanador(guardado, torneo);
            notificarResultadoConfirmado(guardado, torneo);
        } else {
            p.setEstado(EstadoPartido.EN_DISPUTA);
            notificarDisputa(p, torneo);
        }
    }

    private void aplicarGanador(Partido p, boolean ganadorEsJ1, boolean esPorEquipos) {
        if (esPorEquipos) {
            p.setGanadorEquipo(ganadorEsJ1 ? p.getEquipo1() : p.getEquipo2());
        } else {
            p.setGanadorUsuario(ganadorEsJ1 ? p.getJugador1() : p.getJugador2());
        }
    }

    private void avanzarGanador(Partido partido, Torneo torneo) {
        int siguienteRonda = partido.getRonda() + 1;
        int siguientePosicion = partido.getPosicion() / 2;
        boolean esJ1EnSiguiente = partido.getPosicion() % 2 == 0;

        partidoRepository.findByTorneoAndRondaAndPosicion(torneo, siguienteRonda, siguientePosicion)
                .ifPresent(siguiente -> {
                    if (torneo.isEsPorEquipos()) {
                        if (esJ1EnSiguiente) siguiente.setEquipo1(partido.getGanadorEquipo());
                        else siguiente.setEquipo2(partido.getGanadorEquipo());
                    } else {
                        if (esJ1EnSiguiente) siguiente.setJugador1(partido.getGanadorUsuario());
                        else siguiente.setJugador2(partido.getGanadorUsuario());
                    }
                    partidoRepository.save(siguiente);
                });
    }

    private void propagarGanadoresBye(List<Partido> partidos, boolean esPorEquipos) {
        partidos.stream()
                .filter(p -> p.getEstado() == EstadoPartido.CONFIRMADO)
                .forEach(p -> avanzarGanador(p, p.getTorneo()));
    }

    private boolean esParticipante1(Partido partido, String username, boolean esPorEquipos) {
        if (esPorEquipos) {
            // Cualquier miembro del equipo puede reportar
            if (partido.getEquipo1() != null) {
                boolean esMiembro = partido.getEquipo1().getMiembros().stream()
                        .anyMatch(u -> u.getEmail().equals(username));
                if (esMiembro) return true;
            }
            return false;
        } else {
            return partido.getJugador1() != null && partido.getJugador1().getEmail().equals(username);
        }
    }

    private int siguientePotenciaDeDos(int n) {
        if (n <= 0) return 1;
        int p = 1;
        while (p < n) p <<= 1;
        return p;
    }

    private Torneo findTorneo(Long id) {
        return torneoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
    }

    private Partido findPartido(Long id, Torneo torneo) {
        Partido p = partidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));
        if (!p.getTorneo().getId().equals(torneo.getId())) {
            throw new RuntimeException("El partido no pertenece a este torneo");
        }
        return p;
    }

    private void verificarOrganizador(Torneo torneo, String username) {
        boolean esOrg = torneo.getOrganizadores().stream()
                .anyMatch(u -> u.getEmail().equals(username));
        if (!esOrg) throw new RuntimeException("Solo el organizador puede realizar esta acción");
    }

    private void notificarResultadoConfirmado(Partido p, Torneo torneo) {
        String ganador = p.getGanadorUsuario() != null
                ? p.getGanadorUsuario().getUsername()
                : (p.getGanadorEquipo() != null ? p.getGanadorEquipo().getNombre() : "?");
        String nombreTorneo = torneo.getNombre();

        if (!torneo.isEsPorEquipos()) {
            if (p.getJugador1() != null)
                emailService.sendResultConfirmed(p.getJugador1().getEmail(), nombreTorneo, ganador);
            if (p.getJugador2() != null)
                emailService.sendResultConfirmed(p.getJugador2().getEmail(), nombreTorneo, ganador);
        } else {
            if (p.getEquipo1() != null)
                p.getEquipo1().getMiembros().forEach(m ->
                        emailService.sendResultConfirmed(m.getEmail(), nombreTorneo, ganador));
            if (p.getEquipo2() != null)
                p.getEquipo2().getMiembros().forEach(m ->
                        emailService.sendResultConfirmed(m.getEmail(), nombreTorneo, ganador));
        }
    }

    private void notificarDisputa(Partido p, Torneo torneo) {
        torneo.getOrganizadores().forEach(org ->
                emailService.sendDisputePending(org.getEmail(), torneo.getNombre()));
    }

    // ── DTO mapper ────────────────────────────────────────────────

    private PartidoDTO toDto(Partido p) {
        PartidoDTO dto = new PartidoDTO();
        dto.setId(p.getId());
        dto.setRonda(p.getRonda());
        dto.setPosicion(p.getPosicion());
        dto.setEstado(p.getEstado());
        dto.setReporteJ1(p.getReporteJ1());
        dto.setReporteJ2(p.getReporteJ2());

        if (p.getJugador1() != null) {
            dto.setIdJ1(p.getJugador1().getId());
            dto.setNombreJ1(p.getJugador1().getUsername());
        } else if (p.getEquipo1() != null) {
            dto.setIdJ1(p.getEquipo1().getId());
            dto.setNombreJ1(p.getEquipo1().getNombre());
        }

        if (p.getJugador2() != null) {
            dto.setIdJ2(p.getJugador2().getId());
            dto.setNombreJ2(p.getJugador2().getUsername());
        } else if (p.getEquipo2() != null) {
            dto.setIdJ2(p.getEquipo2().getId());
            dto.setNombreJ2(p.getEquipo2().getNombre());
        }

        if (p.getGanadorUsuario() != null) {
            dto.setIdGanador(p.getGanadorUsuario().getId());
            dto.setNombreGanador(p.getGanadorUsuario().getUsername());
        } else if (p.getGanadorEquipo() != null) {
            dto.setIdGanador(p.getGanadorEquipo().getId());
            dto.setNombreGanador(p.getGanadorEquipo().getNombre());
        }

        return dto;
    }
}
