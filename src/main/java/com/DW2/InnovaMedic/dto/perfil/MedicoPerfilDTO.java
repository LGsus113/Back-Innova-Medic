package com.DW2.InnovaMedic.dto.perfil;

import com.DW2.InnovaMedic.entity.Usuario;
import java.util.List;

public record MedicoPerfilDTO(
        Usuario.Sexo sexo,
        String telefono,
        String email,
        List<String> especialidad,
        String numeroColegiado,
        List<DisponibilidadMedicaDTO> disponibilidad
) {
}
