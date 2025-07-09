package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    @Query("SELECT DISTINCT m.especialidad FROM Medico m")
    List<String> findAllDistinctEspecialidades();
    List<Medico> findByEspecialidadIgnoreCase(String especialidad);
}