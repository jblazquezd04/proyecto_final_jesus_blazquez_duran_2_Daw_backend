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
        System.out.println("Ejecutando Data Seeder... Verificando e insertando datos faltantes.");

        // ==========================================
        // 1. USUARIOS
        // ==========================================
        Usuario organizador1 = usuarioRepository.findByUsername("JesusBlazquez").orElseGet(() -> 
            usuarioRepository.save(Usuario.builder()
                .username("JesusBlazquez")
                .email("jesus@example.com")
                .password(passwordEncoder.encode("1234"))
                .modoActual(ModoUsuario.ORGANIZADOR)
                .torneosRealizados(1)
                .premiosOtorgados(500.0)
                .build())
        );

        Usuario organizador2 = usuarioRepository.findByUsername("EVO_Official").orElseGet(() -> 
            usuarioRepository.save(Usuario.builder()
                .username("EVO_Official")
                .email("admin@evo.gg")
                .password(passwordEncoder.encode("1234"))
                .modoActual(ModoUsuario.ORGANIZADOR)
                .torneosRealizados(50)
                .premiosOtorgados(100000.0)
                .build())
        );

        Usuario jugador1 = usuarioRepository.findByUsername("ProPlayer99").orElseGet(() -> 
            usuarioRepository.save(Usuario.builder()
                .username("ProPlayer99")
                .email("pro@example.com")
                .password(passwordEncoder.encode("1234"))
                .modoActual(ModoUsuario.JUGADOR)
                .torneosJugados(10)
                .torneosGanados(4)
                .build())
        );

        Usuario jugador2 = usuarioRepository.findByUsername("NoobMaster").orElseGet(() -> 
            usuarioRepository.save(Usuario.builder()
                .username("NoobMaster")
                .email("noob@example.com")
                .password(passwordEncoder.encode("1234"))
                .modoActual(ModoUsuario.JUGADOR)
                .build())
        );

        // ==========================================
        // 2. JUEGOS
        // ==========================================
        Juego sf6 = juegoRepository.findByNombre("2XKO").orElseGet(() -> 
            juegoRepository.save(Juego.builder()
                .nombre("2XKO")
                .desarrolladora("Riot Games")
                .genero("Fighting")
                .imagenUrl("https://upload.wikimedia.org/wikipedia/commons/8/88/2XKO_game_logo.svg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original")
                .build())
        );

        Juego lol = juegoRepository.findByNombre("League of Legends").orElseGet(() -> 
            juegoRepository.save(Juego.builder()
                .nombre("League of Legends")
                .desarrolladora("Riot Games")
                .genero("MOBA")
                .imagenUrl("https://upload.wikimedia.org/wikipedia/commons/d/d8/League_of_Legends_2019_vector.svg")
                .build())
        );

        Juego Cs2 = juegoRepository.findByNombre("Counter-Strike 2").orElseGet(() -> 
            juegoRepository.save(Juego.builder()
                .nombre("Counter-Strike 2")
                .desarrolladora("Valve")
                .genero("Shooter")
                .imagenUrl("https://upload.wikimedia.org/wikipedia/commons/b/b8/Counter-Strike_2_logo.svg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original")
                .build())
        );

        Juego RL = juegoRepository.findByNombre("Rocket League").orElseGet(() -> 
            juegoRepository.save(Juego.builder()
                .nombre("Rocket League")
                .desarrolladora("Psyonix")
                .genero("Sports")
                .imagenUrl("https://upload.wikimedia.org/wikipedia/commons/c/c3/Rocket_League_logo.svg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original")
                .build())
        );

        Juego Valorant = juegoRepository.findByNombre("Valorant").orElseGet(() -> 
            juegoRepository.save(Juego.builder()
                .nombre("Valorant")
                .desarrolladora("Riot Games")
                .genero("Shooter")
                .imagenUrl("https://upload.wikimedia.org/wikipedia/commons/b/b8/Counter-Strike_2_logo.svg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original")
                .build())
        );

        Juego r6 = juegoRepository.findByNombre("Rainbow Six Siege").orElseGet(() -> 
            juegoRepository.save(Juego.builder()
                .nombre("Rainbow Six Siege")
                .desarrolladora("Ubisoft")
                .genero("Shooter")
                .imagenUrl("https://upload.wikimedia.org/wikipedia/commons/a/ad/Tom_Clancy%27s_Rainbow_Six_logo.svg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original")
                .build())
        );

        Juego CR = juegoRepository.findByNombre("Clash Royale").orElseGet(() -> 
            juegoRepository.save(Juego.builder()
                .nombre("Clash Royale")
                .desarrolladora("Supercell")
                .genero("Strategy")
                .imagenUrl("https://upload.wikimedia.org/wikipedia/commons/5/5a/Supercell-logo.svg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original")
                .build())
        );

        Juego MK = juegoRepository.findByNombre("Mortal Kombat").orElseGet(() -> 
            juegoRepository.save(Juego.builder()
                .nombre("Mortal Kombat")
                .desarrolladora("NetherRealm Studios")
                .genero("Fighting")
                .imagenUrl("https://upload.wikimedia.org/wikipedia/commons/4/45/Mortal_Kombat_logo.svg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original")
                .build())
        );

        // ==========================================
        // 3. EQUIPOS
        // ==========================================
        Equipo equipoT1 = equipoRepository.findByNombre("T1 Esports").orElseGet(() -> 
            equipoRepository.save(Equipo.builder()
                .nombre("T1 Esports")
                .logoUrl("https://upload.wikimedia.org/wikipedia/en/f/f9/T1_logo.svg")
                .miembros(Set.of(jugador1, jugador2))
                .build())
        );

        Equipo equipoFnatic = equipoRepository.findByNombre("Fnatic").orElseGet(() -> 
            equipoRepository.save(Equipo.builder()
                .nombre("Fnatic")
                .logoUrl("https://upload.wikimedia.org/wikipedia/commons/5/59/Fnatic_logo.svg")
                .miembros(Set.of(jugador1, jugador2))
                .build())
        );

        // ==========================================
        // 4. TORNEOS
        // ==========================================
        torneoRepository.findByNombre("Torneo 2XKO Amateur").orElseGet(() -> 
            torneoRepository.save(Torneo.builder()
                .nombre("Torneo 2XKO Amateur")
                .juego(sf6)
                .esPorEquipos(false)
                .organizadores(Set.of(organizador1))
                .participantes(Set.of(jugador1, jugador2))
                .prizePool(500.0)
                .fechaInicio("15/10/2026")
                .estado(EstadoTorneo.PENDIENTE)
                .isTrending(true)
                .esPresencial(true)
                .pais("España")
                .ciudad("Madrid")
                .latitud(40.4168)
                .longitud(-3.7038)
                .build())
        );

        torneoRepository.findByNombre("LoL Worlds Cup").orElseGet(() -> 
            torneoRepository.save(Torneo.builder()
                .nombre("LoL Worlds Cup")
                .juego(lol)
                .esPorEquipos(true)
                .organizadores(Set.of(organizador2))
                .equiposParticipantes(Set.of(equipoT1))
                .prizePool(10000.0)
                .fechaInicio("11/11/2026")
                .estado(EstadoTorneo.PENDIENTE)
                .isTrending(false)
                .esPresencial(true)
                .pais("Canadá")
                .ciudad("Toronto")
                .latitud(43.651070)
                .longitud(-79.347015)
                .build())
        );

        torneoRepository.findByNombre("Torneo Rocket League").orElseGet(() -> 
            torneoRepository.save(Torneo.builder()
                .nombre("Torneo Rocket League")
                .juego(RL)
                .esPorEquipos(false)
                .organizadores(Set.of(organizador1))
                .participantes(Set.of(jugador1, jugador2))
                .prizePool(500.0)
                .fechaInicio("15/11/2026")
                .estado(EstadoTorneo.PENDIENTE)
                .isTrending(true)
                .esPresencial(false)
                .pais("México")
                .ciudad("CDMX")
                .latitud(19.4326)
                .longitud(-99.1332)
                .build())
        );

        torneoRepository.findByNombre("CS2 OPEN FINALE").orElseGet(() -> 
            torneoRepository.save(Torneo.builder()
                .nombre("CS2 OPEN FINALE")
                .juego(Cs2)
                .esPorEquipos(false)
                .organizadores(Set.of(organizador1))
                .participantes(Set.of(jugador1, jugador2))
                .prizePool(1000.0)
                .fechaInicio("10/11/2026")
                .estado(EstadoTorneo.PENDIENTE)
                .isTrending(true)
                .esPresencial(true)
                .pais("México")
                .ciudad("Guadalajara")
                .latitud(20.659724)
                .longitud(-103.349644)
                .build())
        );

        torneoRepository.findByNombre("Fighting Showdown").orElseGet(() -> 
            torneoRepository.save(Torneo.builder()
                .nombre("Fighting Showdown")
                .juego(MK)
                .esPorEquipos(false)
                .organizadores(Set.of(organizador1))
                .participantes(Set.of(jugador1, jugador2))
                .prizePool(500.0)
                .fechaInicio("15/12/2026")
                .estado(EstadoTorneo.PENDIENTE)
                .isTrending(true)
                .esPresencial(true)
                .pais("España")
                .ciudad("Barcelona")
                .latitud(41.3851)
                .longitud(2.1734)
                .build())
        );

        torneoRepository.findByNombre("Mobile Masters").orElseGet(() -> 
            torneoRepository.save(Torneo.builder()
                .nombre("Mobile Masters")
                .juego(CR)
                .esPorEquipos(false)
                .organizadores(Set.of(organizador1))
                .participantes(Set.of(jugador1, jugador2))
                .prizePool(500.0)
                .fechaInicio("15/12/2026")
                .estado(EstadoTorneo.PENDIENTE)
                .isTrending(true)
                .esPresencial(true)
                .pais("España")
                .ciudad("Barcelona")
                .latitud(41.3851)
                .longitud(2.1734)
                .build())
        );

        System.out.println("Data Seeder finalizado. Datos de prueba validados/insertados con éxito.");
    }
}