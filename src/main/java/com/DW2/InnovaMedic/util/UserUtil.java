package com.DW2.InnovaMedic.util;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.entity.Usuario;
import java.util.List;

public class UserUtil {
    public static String role(Usuario usuario) {
        return switch (usuario) {
            case Paciente _ -> "Paciente";
            case Medico _ -> "Medico";
            default -> "Desconocido";
        };
    }

    public static List<CitaDTO> responseCitas(List<Cita> citas) {
        return citas.stream()
                .map(cita -> CitaDTO.fromEntity(cita, cita.getReceta()))
                .toList();
    }

    public static String formatPdfFirebase(Integer idCita) {
        return "recetas/receta-" + idCita + ".pdf";
    }
}
