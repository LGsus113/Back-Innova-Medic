package com.DW2.InnovaMedic.dto;

import java.time.LocalDate;

public record SlotDTO(
        LocalDate fecha,
        String diaSemana,
        String horaInicio,
        String horaFin,
        boolean disponible
) {
}
