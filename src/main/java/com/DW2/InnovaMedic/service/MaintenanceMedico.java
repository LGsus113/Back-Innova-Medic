package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.MedicoSegunEspecialidadDTO;
import com.DW2.InnovaMedic.dto.registro.MedicoRegistroDTO;

import java.util.List;

public interface MaintenanceMedico {
    void registrarMedicos(MedicoRegistroDTO medicoRegistroDTO);
    List<CitaDTO> obtenerCitasMedico(Integer id);
    List<String> obtenerEspecialidadesUnicas();
    List<MedicoSegunEspecialidadDTO> listarMedicosPorEspecialidad(String especialidad);
}
