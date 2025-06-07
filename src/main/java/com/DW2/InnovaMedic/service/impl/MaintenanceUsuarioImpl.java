package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.UsuarioDTO;
import com.DW2.InnovaMedic.entity.Medicos;
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
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe"));

        if (!usuario.getContrasenia().equals(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es incorrecta");
        }

        String rol = obtenerRol(usuario);

        return new UsuarioDTO(usuario.getIdUsuario(), usuario.getNombre(), usuario.getApellido(), rol);
    }

    private String obtenerRol(Usuario usuario) {
        if (usuario instanceof Paciente) {
            return "Paciente";
        } else if (usuario instanceof Medicos) {
            return "Medico";
        } else {
            return "Desconocido";
        }
    }
}
