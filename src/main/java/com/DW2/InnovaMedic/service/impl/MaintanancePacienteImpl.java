package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.repository.PacienteRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenancePaciente;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class MaintanancePacienteImpl implements MaintenancePaciente {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Override
    public void registrarPaciente(Paciente paciente) throws Exception {
        usuarioRepository.findByEmail(paciente.getEmail())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario registrado con el email: " + paciente.getEmail());
                });

        pacienteRepository.save(paciente);
    }
}
