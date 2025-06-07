package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.UsuarioDTO;
import com.DW2.InnovaMedic.entity.Medicos;
import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.entity.Usuario;
import com.DW2.InnovaMedic.repository.MedicoRepository;
import com.DW2.InnovaMedic.repository.PacienteRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintanceUsuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class MaintenanceUsuarioimpl implements MaintanceUsuario {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    MedicoRepository medicoRepository;

    @Override
    public UsuarioDTO validarUsuario(String email, String password) throws Exception {
        String rol = "";

        Optional<Usuario> usuarioOpcional = usuarioRepository.findByEmail(email);
        if (usuarioOpcional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe");
        }

        Usuario usuario = usuarioOpcional.get();
        if (!usuario.getContrasenia().equals(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es incorrecta");
        }

        Optional<Paciente> pacienteOpcional = pacienteRepository.findById(usuario.getIdUsuario());
        Optional<Medicos> medicosOpcional = medicoRepository.findById(usuario.getIdUsuario());

        if (pacienteOpcional.isPresent()) {
            rol = "paciente";
        } else if (medicosOpcional.isPresent()) {
            rol = "medico";
        }

        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                rol
        );
    }
}
