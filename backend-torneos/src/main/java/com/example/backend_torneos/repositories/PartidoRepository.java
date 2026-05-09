package com.example.backend_torneos.repositories;

import com.example.backend_torneos.entities.Partido;
import com.example.backend_torneos.entities.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {
    List<Partido> findByTorneoOrderByRondaAscPosicionAsc(Torneo torneo);
    List<Partido> findByTorneoAndRonda(Torneo torneo, int ronda);
    Optional<Partido> findByTorneoAndRondaAndPosicion(Torneo torneo, int ronda, int posicion);
    void deleteByTorneo(Torneo torneo);
}
