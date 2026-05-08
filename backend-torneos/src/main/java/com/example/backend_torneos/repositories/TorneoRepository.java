package com.example.backend_torneos.repositories;

import com.example.backend_torneos.entities.EstadoTorneo;
import com.example.backend_torneos.entities.Torneo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    Optional<Torneo> findByNombre(String nombre);

    @Query("SELECT t FROM Torneo t WHERE " +
           "(:juegoId IS NULL OR t.juego.id = :juegoId) AND " +
           "(:esPorEquipos IS NULL OR t.esPorEquipos = :esPorEquipos) AND " +
           "(:esPresencial IS NULL OR t.esPresencial = :esPresencial) AND " +
           "(:estado IS NULL OR t.estado = :estado) AND " +
           "(:nombre IS NULL OR LOWER(t.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    Page<Torneo> findConFiltros(
            @Param("juegoId") Long juegoId,
            @Param("esPorEquipos") Boolean esPorEquipos,
            @Param("esPresencial") Boolean esPresencial,
            @Param("estado") EstadoTorneo estado,
            @Param("nombre") String nombre,
            Pageable pageable);
}
