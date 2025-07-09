package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.registro.PacienteRegistroDTO;
import com.DW2.InnovaMedic.entity.Cita;
import java.util.List;

public interface MaintenancePaciente {
    void registrarPaciente(PacienteRegistroDTO pacienteRegistroDTO);
    List<CitaDTO> obtenerCitasPaciente(Integer id, String nombreUsuario, Cita.Estado estado);
}
