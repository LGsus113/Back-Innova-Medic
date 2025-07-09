package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.Historial_Recetas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialRecetasRepository extends JpaRepository<Historial_Recetas, Integer> {
}
