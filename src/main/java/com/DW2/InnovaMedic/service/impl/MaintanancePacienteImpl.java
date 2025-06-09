package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.CitaDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.repository.PacienteRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenancePaciente;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class MaintanancePacienteImpl implements MaintenancePaciente {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    CitaRepository citaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void registrarPaciente(Paciente paciente) throws Exception {
        usuarioRepository.findByEmail(paciente.getEmail())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario registrado con el email: " + paciente.getEmail());
                });

        paciente.setContrasenia(passwordEncoder.encode(paciente.getContrasenia()));
        pacienteRepository.save(paciente);
    }

    @Override
    public List<CitaDTO> obtenerCitasPaciente(Integer id) throws Exception {
        if (!pacienteRepository.existsById(id)) {
            throw  new IllegalArgumentException("Paciente con Id " + id + " no existe");
        }

        List<Cita> citas = citaRepository.findByPaciente_IdUsuario(id);
        return citas.stream()
                .map(CitaDTO::fromEntity)
                .toList();
    }
}
