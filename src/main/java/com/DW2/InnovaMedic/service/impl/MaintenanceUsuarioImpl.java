package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.UsuarioDTO;
import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.entity.Usuario;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintanceUsuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class MaintenanceUsuarioImpl implements MaintanceUsuario {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UsuarioDTO validarUsuario(String email, String password) throws Exception {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email y passwor son obligatorios.");
        }

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Credenciales invalidas",
                        null));

        if (!usuario.getContrasenia().equals(password)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales invalidas",
                    null);
        }

        String rol = obtenerRol(usuario);

        return new UsuarioDTO(usuario.getIdUsuario(), usuario.getNombre(), usuario.getApellido(), rol);
    }

    private String obtenerRol(Usuario usuario) {
        if (usuario instanceof Paciente) {
            return "Paciente";
        } else if (usuario instanceof Medico) {
            return "Medico";
        } else {
            return "Desconocido";
        }
    }
}
