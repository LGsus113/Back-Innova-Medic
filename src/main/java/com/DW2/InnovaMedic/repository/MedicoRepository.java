package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.Medicos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medicos, Integer> {
}