package com.example.backend_torneos.repositories;

import com.example.backend_torneos.entities.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    Optional<Torneo> findByNombre(String nombre);
}
