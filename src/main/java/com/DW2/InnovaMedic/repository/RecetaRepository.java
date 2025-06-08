package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecetaRepository extends JpaRepository<Receta, Integer> {
    Optional<Receta> findByCita_IdCitas(Integer idCita);
}
