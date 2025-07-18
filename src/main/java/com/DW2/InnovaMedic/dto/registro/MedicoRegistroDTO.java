package com.DW2.InnovaMedic.dto.registro;

import com.DW2.InnovaMedic.entity.Usuario;
import java.util.List;

public record MedicoRegistroDTO(
        String nombre,
        String apellido,
        Usuario.Sexo sexo,
        String telefono,
        String email,
        String contrasenia,
        String numeroColegiado,
        String codigoHospital,
        List<Integer> idsEspecialidades
) {
}
