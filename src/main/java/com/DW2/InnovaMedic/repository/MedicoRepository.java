package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    @Query("SELECT m FROM Medico m JOIN m.especialidades e WHERE e.id = :idEspecialidad")
    List<Medico> findMedicosPorIdEspecialidad(@Param("idEspecialidad") Integer idEspecialidad);
}