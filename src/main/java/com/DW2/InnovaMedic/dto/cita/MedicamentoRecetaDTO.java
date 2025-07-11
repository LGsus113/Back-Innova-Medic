package com.DW2.InnovaMedic.dto.cita;

import com.DW2.InnovaMedic.entity.MedicamentoReceta;

public record MedicamentoRecetaDTO(
        Integer idMedicamento,
        String nombreGenerico,
        String nombreFarmaceutico,
        String presentacion,
        String viaAdministracion,
        String dosis,
        String frecuencia,
        String indicacionesUso,
        String duracionTratamiento
) {
    public static MedicamentoRecetaDTO fromEntity (MedicamentoReceta medicamentoReceta) {
        return new MedicamentoRecetaDTO(
                medicamentoReceta.getIdMedicamento(),
                medicamentoReceta.getNombreGenerico(),
                medicamentoReceta.getNombreFarmaceutico(),
                medicamentoReceta.getPresentacion(),
                medicamentoReceta.getViaAdministracion(),
                medicamentoReceta.getDosis(),
                medicamentoReceta.getFrecuencia(),
                medicamentoReceta.getIndicacionesUso(),
                medicamentoReceta.getDuracionTratamiento()
        );
    }
}