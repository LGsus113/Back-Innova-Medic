package com.DW2.InnovaMedic.dto.cita;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaRecetaVaciaDTO(
        Integer idMedico,
        Integer idPaciente,
        Integer idEspecialidad,
        LocalDate fecha,
        LocalTime hora,
        String diagnostico
) {
}
