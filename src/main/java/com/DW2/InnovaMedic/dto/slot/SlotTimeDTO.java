package com.DW2.InnovaMedic.dto.slot;

public record SlotTimeDTO(
        String horaInicio,
        String horaFin,
        boolean disponible
) {
}
