package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.entity.Medicos;
import com.DW2.InnovaMedic.service.MaintenanceMedicos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios/medicos")
public class MedicoController {
    @Autowired
    MaintenanceMedicos maintenanceMedicos;

    @PostMapping("/registrar")
    public String registrarMedico(@RequestBody Medicos medicos) {
        try {
            maintenanceMedicos.registrarMedicos(medicos);
            return "Medico registrado con exito";
        } catch (Exception e) {
            return "Hubo error al registrar usuario: " + e.getMessage();
        }
    }
}
