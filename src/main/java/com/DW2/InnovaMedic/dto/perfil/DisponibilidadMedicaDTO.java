package com.DW2.InnovaMedic.dto.perfil;

import java.time.LocalTime;

public record DisponibilidadMedicaDTO(
        String diaSemana,
        LocalTime horaInicio,
        LocalTime horaFin
) {
}
