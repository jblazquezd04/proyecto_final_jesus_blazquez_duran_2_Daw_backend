package com.example.backend_torneos.repositories;

import com.example.backend_torneos.entities.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {
}
