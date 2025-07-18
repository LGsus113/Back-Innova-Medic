package com.DW2.InnovaMedic.dto.especialidades;

public record ActualizarEspecialidadDTO(
        Integer id,
        String nombreEspecialidad,
        String descripcion,
        Integer idCategoria
) {
}
