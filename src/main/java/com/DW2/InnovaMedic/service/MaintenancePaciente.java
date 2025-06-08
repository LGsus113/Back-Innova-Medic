package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.CitaDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.Paciente;

import java.util.List;

public interface MaintenancePaciente {
    void registrarPaciente(Paciente paciente) throws Exception;
    List<CitaDTO> obtenerCitasPaciente(Integer id) throws Exception;
}
