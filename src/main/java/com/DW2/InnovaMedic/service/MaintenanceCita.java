package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.*;
import com.DW2.InnovaMedic.entity.Cita;
import java.util.List;

public interface MaintenanceCita {
    CitaDTO obtenerCitaCompletaPorID(Integer idCita);

    Integer registrarCitaVacia(CitaRecetaVaciaDTO citaRecetaVaciaDTO);

    String actualizarEstadoCita(Integer idCita, Cita.Estado nuevoEstado);

    void terminarDeRegistrarCitaCompleta(Integer idCita, ActionCitaMedicoDTO request, String nombreMedico);

    void actualizarInformacionMedicaCita(Integer idCita, ActionCitaMedicoDTO request);

    void agregarMedicamento(Integer idCita, List<MedicamentoRecetaRequestDTO> listaMedicamentos);

    void actualizarMedicamento(MedicamentoRecetaDTO medicamentoRecetaDTO);

    void eliminarMedicamento(Integer idMedicamento);
}
