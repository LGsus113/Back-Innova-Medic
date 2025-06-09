package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.CitaDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.repository.MedicoRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenanceMedico;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service
@Transactional
public class MaintenanceMedicoImpl implements MaintenanceMedico {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    MedicoRepository medicoRepository;

    @Autowired
    CitaRepository citaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void registrarMedicos(Medico medico) throws Exception {
        usuarioRepository.findByEmail(medico.getEmail())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario registrado con el email: " + medico.getEmail());
                });

        medico.setContrasenia(passwordEncoder.encode(medico.getContrasenia()));
        medicoRepository.save(medico);
    }

    @Override
    public List<CitaDTO> obtenerCitasMedico(Integer id) throws Exception {
        if (!medicoRepository.existsById(id)) {
            throw new IllegalArgumentException("Medico con Id " + id + " no existe");
        }

        List<Cita> citas = citaRepository.findByMedico_IdUsuario(id);
        return citas.stream()
                .map(CitaDTO::fromEntity)
                .toList();
    }
}
