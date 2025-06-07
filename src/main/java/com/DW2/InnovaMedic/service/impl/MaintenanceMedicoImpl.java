package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.repository.MedicoRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenanceMedico;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class MaintenanceMedicoImpl implements MaintenanceMedico {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    MedicoRepository medicoRepository;

    @Override
    public void registrarMedicos(Medico medico) throws Exception {
        usuarioRepository.findByEmail(medico.getEmail())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario registrado con el email: " + medico.getEmail());
                });

        medicoRepository.save(medico);
    }
}
