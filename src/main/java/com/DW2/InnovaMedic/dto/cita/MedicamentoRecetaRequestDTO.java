package com.DW2.InnovaMedic.dto.cita;

public record MedicamentoRecetaRequestDTO(
        String nombreGenerico,
        String nombreFarmaceutico,
        String presentacion,
        String viaAdministracion,
        String dosis,
        String frecuencia,
        String indicacionesUso,
        String duracionTratamiento
) {
}
