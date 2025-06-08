package com.DW2.InnovaMedic.dto;

import com.DW2.InnovaMedic.entity.Cita;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaDTO(
        Integer idCitas,
        MedicoResumenDTO medico,
        PacienteResumenDTO paciente,
        LocalDate fecha,
        LocalTime hora,
        String tratamiento,
        String notasMedicas,
        String diagnostico,
        String estado
) {
    public static CitaDTO fromEntity(Cita cita) {
        return new CitaDTO(
                cita.getIdCitas(),
                MedicoResumenDTO.fromEntity(cita.getMedico()),
                PacienteResumenDTO.fromEntity(cita.getPaciente()),
                cita.getFecha(),
                cita.getHora(),
                cita.getTratamiento(),
                cita.getNotasMedicas(),
                cita.getDiagnostico(),
                cita.getEstado().name()
        );
    }
}
