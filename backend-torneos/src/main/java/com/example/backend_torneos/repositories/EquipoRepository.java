package com.example.backend_torneos.repositories;

import com.example.backend_torneos.entities.Equipo;
import com.example.backend_torneos.entities.Torneo;
import com.example.backend_torneos.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    Optional<Equipo> findByNombre(String nombre);
    List<Equipo> findByTorneo(Torneo torneo);
    Optional<Equipo> findByTorneoAndMiembrosContaining(Torneo torneo, Usuario usuario);
    List<Equipo> findByMiembrosContaining(Usuario usuario);
}
