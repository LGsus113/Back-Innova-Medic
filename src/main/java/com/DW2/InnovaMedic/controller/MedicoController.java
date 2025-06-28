package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.registro.MedicoRegistroDTO;
import com.DW2.InnovaMedic.service.MaintenanceMedico;
import com.DW2.InnovaMedic.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {
    @Autowired
    MaintenanceMedico maintenanceMedico;

    @GetMapping("/cita/{id}")
    public ResponseEntity<?> listaCitasMedico(@PathVariable Integer id) {
        try {
            List<CitaDTO> citasMedico = maintenanceMedico.obtenerCitasMedico(id);

            if (citasMedico.isEmpty()) {
                return ResponseUtil.successWith(Map.of(
                        "status", "success",
                        "message", "El médico existe, pero no tiene citas registradas.",
                        "idMedico", id
                ));
            }

            return ResponseUtil.success(citasMedico);
        } catch (IllegalArgumentException ie) {
            return ResponseUtil.error(HttpStatus.NOT_FOUND, "Médico no encontrado: " + ie.getMessage());
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error interno al buscar citas del médico con ID " + id + ": " + e.getMessage());
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMedico(@RequestBody MedicoRegistroDTO medicoRegistroDTO) {
        try {
            maintenanceMedico.registrarMedicos(medicoRegistroDTO);
            return ResponseUtil.successMessage("Usuario registrado con éxito");
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST,
                    "Hubo un error al registrar usuario: " + e.getMessage());
        }
    }
}