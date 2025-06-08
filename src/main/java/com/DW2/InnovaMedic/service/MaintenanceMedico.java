package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.CitaDTO;
import com.DW2.InnovaMedic.entity.Medico;

import java.util.List;

public interface MaintenanceMedico {
    void registrarMedicos(Medico medico) throws Exception;
    List<CitaDTO> obtenerCitasMedico(Integer id) throws Exception;
}
