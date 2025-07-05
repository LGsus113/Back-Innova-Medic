package com.DW2.InnovaMedic.dto.slot;

import java.time.LocalDate;
import java.util.List;

public record SlotPorDiaDTO(
        LocalDate fecha,
        String diaSemana,
        List<SlotTimeDTO> slots
) {
}
