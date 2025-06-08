package com.DW2.InnovaMedic.dto;

public record ActualizarCitaCompletaDTO(
        Integer id,
        ActionCitaMedicoDTO actionCitaMedicoDTO,
        String nombreMedico
) {
}
