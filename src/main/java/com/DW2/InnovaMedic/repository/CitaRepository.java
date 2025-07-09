package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.dto.projections.CitaHorarioProjection;
import com.DW2.InnovaMedic.entity.Cita;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer>, JpaSpecificationExecutor<Cita> {
    @EntityGraph(attributePaths = {"receta", "receta.medicamentos"})
    List<Cita> findAll(Specification<Cita> spec);

    @Query("""
            SELECT c FROM Cita c
                     LEFT JOIN FETCH c.receta r
                     LEFT JOIN FETCH r.medicamentos
                     WHERE c.idCitas = :idCita
            """)
    Optional<Cita> findByIdWithRecetaAndMedicamentos(@Param("idCita") Integer idCita);

    @Query("""
                SELECT c.fecha AS fecha, c.hora AS hora
                FROM Cita c
                WHERE c.medico.idUsuario = :idMedico
                AND c.fecha BETWEEN :fechaInicio AND :fechaFin
                AND c.estado IN ('Pendiente', 'Confirmada')
            """)
    List<CitaHorarioProjection> obtenerCitasActivasEnRango(
            @Param("idMedico") Integer idMedico,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
}
