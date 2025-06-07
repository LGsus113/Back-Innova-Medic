package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.service.MaintenancePaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios/pacientes")
public class PacienteController {
    @Autowired
    MaintenancePaciente maintenancePaciente;

    @PostMapping("/registrar")
    public String registrarPaciente(@RequestBody Paciente paciente) {
        try {
            maintenancePaciente.registrarPaciente(paciente);
            return "Paciente registrado con exito";
        } catch (Exception e) {
            return "Hubo error al registrar usuario: " + e.getMessage();
        }
    }
}
