package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.HistorialRecetas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HistorialRecetasRepository extends JpaRepository<HistorialRecetas, Integer> {
    Optional<HistorialRecetas> findByCita_IdCitas(Integer idCitas);
}
