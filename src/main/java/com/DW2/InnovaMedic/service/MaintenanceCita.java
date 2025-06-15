package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.ActionCitaMedicoDTO;
import com.DW2.InnovaMedic.dto.CitaRecetaVaciaDTO;
import com.DW2.InnovaMedic.entity.Cita;

public interface MaintenanceCita {
    Integer registrarCitaVacia(CitaRecetaVaciaDTO citaRecetaVaciaDTO) throws Exception;
    String actualizarEstadoCita(Integer idCita, Cita.Estado nuevoEstado) throws Exception;
    void actualizarCitaCompleta (Integer idCita, ActionCitaMedicoDTO request, String nombreMedico) throws Exception;
}
