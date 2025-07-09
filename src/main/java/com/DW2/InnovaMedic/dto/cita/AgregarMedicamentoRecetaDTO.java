package com.DW2.InnovaMedic.dto.cita;

import java.util.List;

public record AgregarMedicamentoRecetaDTO(
        Integer idCita,
        List<MedicamentoRecetaRequestDTO> listaMedicamentos
) {
}
