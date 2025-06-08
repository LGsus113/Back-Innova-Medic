package com.DW2.InnovaMedic.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaRecetaVaciaDTO(
        Integer idMedico,
        Integer idPaciente,
        LocalDate fecha,
        LocalTime hora,
        String tratamiento
) {
}
