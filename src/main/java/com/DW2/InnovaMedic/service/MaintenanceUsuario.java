package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.entity.Usuario;
import java.util.Optional;

public interface MaintenanceUsuario {
    Optional<Usuario> buscarPorEmail(String email);
    Object obtenerUsuarioPorId(Integer idUsuario);
}
