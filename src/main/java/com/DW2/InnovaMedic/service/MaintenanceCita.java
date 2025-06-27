package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.ActionCitaMedicoDTO;
import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.CitaRecetaVaciaDTO;
import com.DW2.InnovaMedic.entity.Cita;

public interface MaintenanceCita {
    CitaDTO obtenerCitaCompletaPorID(Integer idCita) throws Exception;
    Integer registrarCitaVacia(CitaRecetaVaciaDTO citaRecetaVaciaDTO) throws Exception;
    String actualizarEstadoCita(Integer idCita, Cita.Estado nuevoEstado) throws Exception;
    void actualizarCitaCompleta (Integer idCita, ActionCitaMedicoDTO request, String nombreMedico) throws Exception;
}
