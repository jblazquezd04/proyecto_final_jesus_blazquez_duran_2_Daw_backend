package com.example.backend_torneos.config;

import com.example.backend_torneos.entities.*;
import com.example.backend_torneos.repositories.EquipoRepository;
import com.example.backend_torneos.repositories.JuegoRepository;
import com.example.backend_torneos.repositories.TorneoRepository;
import com.example.backend_torneos.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

        private final UsuarioRepository usuarioRepository;
        private final TorneoRepository torneoRepository;
        private final JuegoRepository juegoRepository;
        private final EquipoRepository equipoRepository;
        private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) throws Exception {
                if (torneoRepository.count() == 0) {
                        System.out.println("No hay torneos en la base de datos. Insertando datos de prueba...");

                        // 1. Usuarios
                        Usuario organizador1 = Usuario.builder()
                                        .username("JesusBlazquez")
                                        .email("jesus@example.com")
                                        .password(passwordEncoder.encode("1234"))
                                        .modoActual(ModoUsuario.ORGANIZADOR)
                                        .torneosRealizados(1)
                                        .premiosOtorgados(500.0)
                                        .build();

                        Usuario organizador2 = Usuario.builder()
                                        .username("EVO_Official")
                                        .email("admin@evo.gg")
                                        .password(passwordEncoder.encode("1234"))
                                        .modoActual(ModoUsuario.ORGANIZADOR)
                                        .torneosRealizados(50)
                                        .premiosOtorgados(100000.0)
                                        .build();

                        Usuario jugador1 = Usuario.builder()
                                        .username("ProPlayer99")
                                        .email("pro@example.com")
                                        .password(passwordEncoder.encode("1234"))
                                        .modoActual(ModoUsuario.JUGADOR)
                                        .torneosJugados(10)
                                        .torneosGanados(4)
                                        .build();

                        Usuario jugador2 = Usuario.builder()
                                        .username("NoobMaster")
                                        .email("noob@example.com")
                                        .password(passwordEncoder.encode("1234"))
                                        .modoActual(ModoUsuario.JUGADOR)
                                        .build();

                        usuarioRepository.save(organizador1);
                        usuarioRepository.save(organizador2);
                        usuarioRepository.save(jugador1);
                        usuarioRepository.save(jugador2);

                        // 2. Juegos
                        Juego sf6 = Juego.builder()
                                        .nombre("2XKO")
                                        .desarrolladora("Riot Games")
                                        .genero("Fighting")
                                        .imagenUrl("https://upload.wikimedia.org/wikipedia/commons/8/88/2XKO_game_logo.svg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original")
                                        .build();

                        Juego lol = Juego.builder()
                                        .nombre("League of Legends")
                                        .desarrolladora("Riot Games")
                                        .genero("MOBA")
                                        .imagenUrl("https://upload.wikimedia.org/wikipedia/commons/d/d8/League_of_Legends_2019_vector.svg")
                                        .build();

                        juegoRepository.save(sf6);
                        juegoRepository.save(lol);

                        // 3. Equipos
                        Equipo equipoT1 = Equipo.builder()
                                        .nombre("T1 Esports")
                                        .logoUrl("https://upload.wikimedia.org/wikipedia/en/f/f9/T1_logo.svg")
                                        .miembros(Set.of(jugador1, jugador2))
                                        .build();

                        equipoRepository.save(equipoT1);

                        // 4. Torneos
                        Torneo torneoIndividual = Torneo.builder()
                                        .nombre("Torneo 2XKO Amateur")
                                        .juego(sf6)
                                        .esPorEquipos(false)
                                        .organizadores(Set.of(organizador1))
                                        .participantes(Set.of(jugador1, jugador2))
                                        .prizePool(500.0)
                                        .fechaInicio("20/10/2025")
                                        .estado(EstadoTorneo.PENDIENTE)
                                        .isTrending(true)
                                        .esPresencial(true)
                                        .pais("España")
                                        .ciudad("Madrid")
                                        .latitud(40.4168)
                                        .longitud(-3.7038)
                                        .build();

                        Torneo torneoEquipos = Torneo.builder()
                                        .nombre("LoL Worlds Cup")
                                        .juego(lol)
                                        .esPorEquipos(true)
                                        .organizadores(Set.of(organizador2))
                                        .equiposParticipantes(Set.of(equipoT1))
                                        .prizePool(10000.0)
                                        .fechaInicio("28/10/2025")
                                        .estado(EstadoTorneo.PENDIENTE)
                                        .isTrending(false)
                                        .esPresencial(true)
                                        .pais("Canadá")
                                        .ciudad("Toronto")
                                        .latitud(43.651070)
                                        .longitud(-79.347015)
                                        .build();

                        torneoRepository.save(torneoIndividual);
                        torneoRepository.save(torneoEquipos);

                        System.out.println("Datos de prueba insertados con éxito.");
                }
        }
}
