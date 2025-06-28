package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.*;
import com.DW2.InnovaMedic.entity.Cita;

import java.util.List;

public interface MaintenanceCita {
    CitaDTO obtenerCitaCompletaPorID(Integer idCita) throws Exception;

    Integer registrarCitaVacia(CitaRecetaVaciaDTO citaRecetaVaciaDTO) throws Exception;

    String actualizarEstadoCita(Integer idCita, Cita.Estado nuevoEstado) throws Exception;

    void terminarDeRegistrarCitaCompleta(Integer idCita, ActionCitaMedicoDTO request, String nombreMedico) throws Exception;

    void actualizarInformacionMedicaCita(Integer idCita, ActionCitaMedicoDTO request) throws Exception;

    void agregarMedicamento(Integer idCita, List<MedicamentoRecetaRequestDTO> listaMedicamentos) throws Exception;

    void actualizarMedicamento(MedicamentoRecetaDTO medicamentoRecetaDTO) throws Exception;

    void eliminarMedicamento(Integer idMedicamento) throws Exception;
}
