package com.DW2.InnovaMedic.dto.cita;

import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.Receta;
import java.time.LocalDate;
import java.time.LocalTime;

public record CitaDTO(
        Integer idCitas,
        MedicoResumenDTO medico,
        PacienteResumenDTO paciente,
        String especialidad,
        LocalDate fecha,
        LocalTime hora,
        String notasMedicas,
        String diagnostico,
        String estado,
        RecetaDTO recetaDTO
) {
    public static CitaDTO fromEntity(Cita cita, Receta receta) {
        return new CitaDTO(
                cita.getIdCitas(),
                MedicoResumenDTO.fromEntity(cita.getMedico()),
                PacienteResumenDTO.fromEntity(cita.getPaciente()),
                cita.getEspecialidad().getNombreEspecialidad(),
                cita.getFecha(),
                cita.getHora(),
                cita.getNotasMedicas(),
                cita.getDiagnostico(),
                cita.getEstado().name(),
                receta != null ? RecetaDTO.fromEntity(receta) : null
        );
    }
}
