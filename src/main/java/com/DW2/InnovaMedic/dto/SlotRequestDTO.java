package com.DW2.InnovaMedic.dto;

import java.time.LocalDate;

public record SlotRequestDTO(
        Integer idMedico,
        LocalDate fechaInicio,
        LocalDate fechaFin
) {
}
