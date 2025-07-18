package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.CategoriaEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaEspecialidadRepository extends JpaRepository<CategoriaEspecialidad, Integer> {
}
