package com.DW2.InnovaMedic.dto.cita;

import com.DW2.InnovaMedic.entity.MedicamentoReceta;

public record MedicamentoRecetaDTO(
        Integer idMedicamento,
        String nombre,
        String dosis,
        String frecuencia
) {
    public static MedicamentoRecetaDTO fromEntity (MedicamentoReceta medicamentoReceta) {
        return new MedicamentoRecetaDTO(
                medicamentoReceta.getIdMedicamento(),
                medicamentoReceta.getNombre(),
                medicamentoReceta.getDosis(),
                medicamentoReceta.getFrecuencia()
        );
    }
}