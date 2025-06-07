package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.UsuarioDTO;

public interface MaintanceUsuario {
    UsuarioDTO validarUsuario(String email, String password) throws Exception;
}
