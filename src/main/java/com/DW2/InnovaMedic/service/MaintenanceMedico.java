package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.CitaDTO;
import com.DW2.InnovaMedic.dto.MedicoRegistroDTO;

import java.util.List;

public interface MaintenanceMedico {
    void registrarMedicos(MedicoRegistroDTO medicoRegistroDTO) throws Exception;
    List<CitaDTO> obtenerCitasMedico(Integer id) throws Exception;
}
