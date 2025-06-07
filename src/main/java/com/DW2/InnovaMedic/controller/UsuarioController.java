package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.LoginRequestDTO;
import com.DW2.InnovaMedic.dto.UsuarioDTO;
import com.DW2.InnovaMedic.service.MaintanceUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    MaintanceUsuario maintanceUsuario;

    @PostMapping("/log")
    public String registrarMedico(@RequestBody LoginRequestDTO login) {
        try {
            UsuarioDTO usuarioDTO = maintanceUsuario.validarUsuario(login.email(), login.password());
            return "Bienvenido al sistema " + usuarioDTO.rol() + " " + usuarioDTO.nombre() + " " + usuarioDTO.apellido();
        } catch (Exception e) {
            return "Error validando tus credenciales: " + e.getMessage();
        }
    }
}
