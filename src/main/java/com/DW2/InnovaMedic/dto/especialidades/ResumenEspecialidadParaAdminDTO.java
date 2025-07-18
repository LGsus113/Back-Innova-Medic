package com.DW2.InnovaMedic.dto.especialidades;

public record ResumenEspecialidadParaAdminDTO(
        Integer id,
        String nombreEspecialidad,
        String descripcion,
        Boolean visible
) {
}
