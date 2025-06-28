package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.MedicoSegunEspecialidadDTO;
import com.DW2.InnovaMedic.dto.registro.PacienteRegistroDTO;
import com.DW2.InnovaMedic.service.MaintenanceMedico;
import com.DW2.InnovaMedic.service.MaintenancePaciente;
import com.DW2.InnovaMedic.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {
    @Autowired
    MaintenancePaciente maintenancePaciente;

    @Autowired
    MaintenanceMedico maintenanceMedico;

    @GetMapping("/cita/{id}")
    public ResponseEntity<?> listaCitasPaciente(@PathVariable Integer id) {
        try {
            List<CitaDTO> citasPaciente = maintenancePaciente.obtenerCitasPaciente(id);

            if (citasPaciente.isEmpty()) {
                return ResponseUtil.successWith(Map.of(
                        "status", "success",
                        "message", "El paciente existe, pero no tiene citas registradas.",
                        "idPaciente", id
                ));
            }

            return ResponseUtil.success(citasPaciente);
        } catch (IllegalArgumentException ie) {
            return ResponseUtil.error(HttpStatus.NOT_FOUND, "Paciente no encontrado: " + ie.getMessage());
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error interno al buscar citas del paciente con ID " + id + ": " + e.getMessage());
        }
    }

    @GetMapping("/especialidades")
    public ResponseEntity<?> listarEspecialidadesUnicas() {
        try {
            List<String> especialidades = maintenanceMedico.obtenerEspecialidadesUnicas();

            if (especialidades.isEmpty()) {
                return ResponseUtil.successWith(Map.of(
                        "status", "success",
                        "message", "No se encontraron especialidades disponibles"
                ));
            }

            return ResponseUtil.success(especialidades);
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Hubo un error al listar las especialidades: " + e.getMessage());
        }
    }

    @GetMapping("/lista-medicos")
    public ResponseEntity<?> listaMedicosPorEspecialidad(@RequestParam String especialidad) {
        try {
            List<MedicoSegunEspecialidadDTO> medicos = maintenanceMedico.listarMedicosPorEspecialidad(especialidad);

            if (medicos.isEmpty()) {
                return ResponseUtil.successWith(Map.of(
                        "status", "success",
                        "message", "No hay médicos registrados con la especialidad: " + especialidad
                ));
            }

            return ResponseUtil.success(medicos);
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST,
                    "Hubo un error al obtener los médicos por especialidad: " + e.getMessage());
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPaciente(@RequestBody PacienteRegistroDTO pacienteRegistroDTO) {
        try {
            maintenancePaciente.registrarPaciente(pacienteRegistroDTO);
            return ResponseUtil.successMessage("Usuario registrado con éxito");
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST,
                    "Hubo un error al registrar usuario: " + e.getMessage());
        }
    }
}