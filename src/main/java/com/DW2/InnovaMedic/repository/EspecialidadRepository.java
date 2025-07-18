package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
    List<Especialidad> findByVisibleTrue();
    Optional<Especialidad> findByNombreEspecialidad(String nombreEspecialidad);
    boolean existsByNombreEspecialidadAndIdNot(String nombreEspecialidad, Integer id);

    @Query("SELECT COUNT(m) FROM Medico m JOIN m.especialidades e WHERE e.id = :idEspecialidad")
    Integer contarMedicosPorEspecialidad(@Param("idEspecialidad") Integer idEspecialidad);
}
