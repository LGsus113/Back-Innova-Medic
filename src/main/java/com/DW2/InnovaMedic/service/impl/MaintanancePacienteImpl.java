package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.repository.PacienteRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenancePaciente;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MaintanancePacienteImpl implements MaintenancePaciente {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Override
    public void registrarPaciente(Paciente paciente) throws Exception {
        boolean existe = usuarioRepository.findByEmail(paciente.getEmail()).isPresent();
        if (existe) {
            throw new Exception("Ya existe un usuario registrado con el email: " + paciente.getEmail());
        }

        pacienteRepository.save(paciente);
    }
}
