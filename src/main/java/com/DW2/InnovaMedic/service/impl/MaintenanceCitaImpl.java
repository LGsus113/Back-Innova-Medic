package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.ActionCitaMedicoDTO;
import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.CitaRecetaVaciaDTO;
import com.DW2.InnovaMedic.dto.cita.MedicamentoRecetaRequestDTO;
import com.DW2.InnovaMedic.entity.*;
import com.DW2.InnovaMedic.repository.*;
import com.DW2.InnovaMedic.service.MaintenanceCita;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceCitaImpl implements MaintenanceCita {
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final RecetaRepository recetaRepository;
    private final MedicamentoRecetaRepository medicamentoRecetaRepository;

    @Override
    public CitaDTO obtenerCitaCompletaPorID(Integer idCita) throws Exception {
        Cita cita = citaRepository.findByIdWithRecetaAndMedicamentos(idCita)
                .orElseThrow(() -> new Exception("Cita no encontrada con ID: " + idCita));

        return CitaDTO.fromEntity(cita, cita.getReceta());
    }

    @Override
    @CacheEvict(value = {"citasPaciente", "citasMedico", "slotsDisponibles"}, allEntries = true)
    public Integer registrarCitaVacia(CitaRecetaVaciaDTO citaRecetaVaciaDTO) throws Exception {
        if (citaRecetaVaciaDTO.fecha() == null || citaRecetaVaciaDTO.hora() == null) {
            throw new IllegalArgumentException("Fecha y hora son requeridos");
        }

        Medico medico = medicoRepository.findById(citaRecetaVaciaDTO.idMedico())
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));

        Paciente paciente = pacienteRepository.findById(citaRecetaVaciaDTO.idPaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        Cita cita = new Cita();
        cita.setMedico(medico);
        cita.setPaciente(paciente);
        cita.setFecha(citaRecetaVaciaDTO.fecha());
        cita.setHora(citaRecetaVaciaDTO.hora());
        cita.setTratamiento(citaRecetaVaciaDTO.tratamiento());
        cita.setNotasMedicas("aun no detallado");
        cita.setDiagnostico("aun no detallado");
        cita.setEstado(Cita.Estado.Pendiente);

        Cita citaGuardada = citaRepository.save(cita);

        Receta receta = new Receta();
        receta.setCita(citaGuardada);
        receta.setFecha(citaRecetaVaciaDTO.fecha());
        receta.setInstruccionesAdicionales("aun no detallado");
        receta.setFirmaMedico("aun no detallado");

        recetaRepository.save(receta);

        return citaGuardada.getIdCitas();
    }

    private void validarTransicionEstado(Cita.Estado estadoActual, Cita.Estado nuevoEstado) {
        if (estadoActual == nuevoEstado) return;

        switch (estadoActual) {
            case Pendiente:
                if (!List.of(Cita.Estado.Confirmada, Cita.Estado.Cancelada).contains(nuevoEstado)) {
                    throw new IllegalStateException(
                            "Una cita Pendiente solo puede cambiarse a Confirmada o Cancelada"
                    );
                }
                break;

            case Confirmada:
                if (!List.of(Cita.Estado.Finalizada, Cita.Estado.Cancelada).contains(nuevoEstado)) {
                    throw new IllegalStateException(
                            "Una cita Confirmada solo puede cambiarse a Finalizada o Cancelada"
                    );
                }
                break;

            case Cancelada:
            case Finalizada:
                throw new IllegalArgumentException(
                        "Una cita " + estadoActual + " no puede cambiar de estado"
                );

            default:
                throw new IllegalArgumentException(
                        "Estado no reconocido: " + estadoActual
                );
        }
    }

    @Override
    @CacheEvict(value = {"citasPaciente", "citasMedico", "slotsDisponibles"}, allEntries = true)
    public String actualizarEstadoCita(Integer idCita, Cita.Estado nuevoEstado) throws Exception {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }

        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la cita con ID: " + idCita
                ));

        validarTransicionEstado(cita.getEstado(), nuevoEstado);

        cita.setEstado(nuevoEstado);

        return "Estado de la cita actualizado correctamente a: " + nuevoEstado;
    }

    private void actualizarInformacionCita(Integer idCita, String notasMedicas, String diagnostico) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cita " + idCita + " no existe"));

        cita.setNotasMedicas(notasMedicas);
        cita.setDiagnostico(diagnostico);
        citaRepository.save(cita);
    }

    private void actualizarReceta(Integer idCita, String instruccionesAdicionales, String firmaMedico) {
        Receta receta = recetaRepository.findByCita_IdCitas(idCita)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La receta " + idCita + " no existe"));

        receta.setInstruccionesAdicionales(instruccionesAdicionales);
        receta.setFirmaMedico(firmaMedico);
        recetaRepository.save(receta);
    }

    private void medicamentosReceta(Integer idCita, List<MedicamentoRecetaRequestDTO> listaMedicamentos) {
        Receta receta = recetaRepository.findByCita_IdCitas(idCita)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Receta no encontrada para la cita con ID: " + idCita
                ));

        List<MedicamentoReceta> medicamentosActuales = medicamentoRecetaRepository.findByReceta_IdReceta(receta.getIdReceta());

        for (MedicamentoReceta actual : medicamentosActuales) {
            boolean existeEnNuevos = listaMedicamentos.stream().anyMatch(nuevo ->
                    actual.getNombre().equalsIgnoreCase(nuevo.nombre().trim()) &&
                            actual.getDosis().equalsIgnoreCase(nuevo.dosis().trim()) &&
                            actual.getFrecuencia().equalsIgnoreCase(nuevo.frecuencia().trim())
            );

            if (!existeEnNuevos) {
                medicamentoRecetaRepository.delete(actual);
            }
        }

        for (MedicamentoRecetaRequestDTO nuevo : listaMedicamentos) {
            boolean yaExiste = medicamentosActuales.stream().anyMatch(actual ->
                    actual.getNombre().equalsIgnoreCase(nuevo.nombre().trim()) &&
                            actual.getDosis().equalsIgnoreCase(nuevo.dosis().trim()) &&
                            actual.getFrecuencia().equalsIgnoreCase(nuevo.frecuencia().trim())
            );

            if (!yaExiste) {
                MedicamentoReceta mr = new MedicamentoReceta();
                mr.setReceta(receta);
                mr.setNombre(nuevo.nombre().trim());
                mr.setDosis(nuevo.dosis().trim());
                mr.setFrecuencia(nuevo.frecuencia().trim());
                medicamentoRecetaRepository.save(mr);
            }
        }
    }

    @Override
    @CacheEvict(value = {"citasPaciente", "citasMedico", "slotsDisponibles"}, allEntries = true)
    public void actualizarCitaCompleta(Integer idCita, ActionCitaMedicoDTO actionCitaMedicoDTO, String nombreMedico) {
        actualizarInformacionCita(idCita, actionCitaMedicoDTO.notasMedicas(), actionCitaMedicoDTO.diagnostico());

        actualizarReceta(idCita, actionCitaMedicoDTO.instruccionesAdicionales(), nombreMedico);

        medicamentosReceta(idCita, actionCitaMedicoDTO.medicamentos());
    }
}
