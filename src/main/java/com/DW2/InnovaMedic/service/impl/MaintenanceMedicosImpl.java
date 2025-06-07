package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.entity.Medicos;
import com.DW2.InnovaMedic.repository.MedicoRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenanceMedicos;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MaintenanceMedicosImpl implements MaintenanceMedicos {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    MedicoRepository medicoRepository;

    @Override
    public void registrarMedicos(Medicos medicos) throws Exception {
        boolean existe = usuarioRepository.findByEmail(medicos.getEmail()).isPresent();
        if (existe) {
            throw new Exception("Ya existe un usuario registrado con el email: " + medicos.getEmail());
        }

        medicoRepository.save(medicos);
    }
}
