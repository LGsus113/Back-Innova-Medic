package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.CitaDTO;
import com.DW2.InnovaMedic.dto.PacienteRegistroDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.repository.PacienteRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenancePaciente;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    PasswordEncoder passwordEncoder;

    @Override
    public void registrarPaciente(PacienteRegistroDTO pacienteRegistroDTO) throws Exception {
        usuarioRepository.findOneByEmail(pacienteRegistroDTO.email())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario registrado con el email: " + pacienteRegistroDTO.email());
                });

        Paciente paciente = new Paciente();
        paciente.setNombre(pacienteRegistroDTO.nombre());
        paciente.setApellido(pacienteRegistroDTO.apellido());
        paciente.setSexo(pacienteRegistroDTO.sexo());
        paciente.setTelefono(pacienteRegistroDTO.telefono());
        paciente.setEmail(pacienteRegistroDTO.email());
        paciente.setContrasenia(passwordEncoder.encode(pacienteRegistroDTO.contrasenia()));
        paciente.setFechaNacimiento(pacienteRegistroDTO.fechaNacimiento());
        paciente.setTalla(pacienteRegistroDTO.talla());
        paciente.setGrupoSanguineo(pacienteRegistroDTO.grupoSanguineo());
        paciente.setDireccion(pacienteRegistroDTO.direccion());

        pacienteRepository.save(paciente);
    }

    @Override
    public List<CitaDTO> obtenerCitasPaciente(Integer id) throws Exception {
        if (!pacienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Paciente con Id " + id + " no existe");
        }

        List<Cita> citas = citaRepository.findByPacienteWithRecetasAndMedicamentos(id);
        return citas.stream()
                .map(cita -> CitaDTO.fromEntity(cita, cita.getReceta()))
                .toList();
    }
}
