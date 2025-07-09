package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.MedicoSegunEspecialidadDTO;
import com.DW2.InnovaMedic.dto.registro.PacienteRegistroDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.service.MaintenanceMedico;
import com.DW2.InnovaMedic.service.MaintenancePaciente;
import com.DW2.InnovaMedic.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pacientes")
public class PacienteController {
    private final MaintenancePaciente maintenancePaciente;
    private final MaintenanceMedico maintenanceMedico;

    @GetMapping("/cita/{id}")
    public ResponseEntity<?> listaCitasPaciente(@PathVariable Integer id, @RequestParam(required = false) Cita.Estado estado, @RequestParam(required = false) String nombreUsuario) {
        List<CitaDTO> citasPaciente = maintenancePaciente.obtenerCitasPaciente(id, nombreUsuario, estado);

        if (citasPaciente.isEmpty()) {
            return ResponseUtil.successWith(Map.of(
                    "message", "El paciente existe, pero no tiene citas registradas.",
                    "idPaciente", id
            ));
        }

        return ResponseUtil.success(citasPaciente);
    }

    @GetMapping("/especialidades")
    public ResponseEntity<?> listarEspecialidadesUnicas() {
        List<String> especialidades = maintenanceMedico.obtenerEspecialidadesUnicas();

        if (especialidades.isEmpty()) {
            return ResponseUtil.successMessage("No se encontraron especialidades disponibles");
        }

        return ResponseUtil.success(especialidades);
    }

    @GetMapping("/lista-medicos")
    public ResponseEntity<?> listaMedicosPorEspecialidad(@RequestParam String especialidad) {
        List<MedicoSegunEspecialidadDTO> medicos = maintenanceMedico.listarMedicosPorEspecialidad(especialidad);

        if (medicos.isEmpty()) {
            return ResponseUtil.successMessage("No hay médicos registrados con la especialidad: " + especialidad);
        }

        return ResponseUtil.success(medicos);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPaciente(@RequestBody PacienteRegistroDTO pacienteRegistroDTO) {
        maintenancePaciente.registrarPaciente(pacienteRegistroDTO);
        return ResponseUtil.successMessage("Usuario registrado con éxito");
    }
}