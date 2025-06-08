package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.CitaRecetaVaciaDTO;
import com.DW2.InnovaMedic.service.MaintenanceCita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cita")
public class CitaController {
    @Autowired
    MaintenanceCita maintenanceCita;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCitaRecetaVacia(@RequestBody CitaRecetaVaciaDTO citaRecetaVaciaDTO) {
        try {
            Integer idCita = maintenanceCita.registrarCitaVacia(citaRecetaVaciaDTO);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", Map.of("idCita", idCita)
            ));
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", ie.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error interno al registrar cita: " + e.getMessage()
            ));
        }
    }
}
