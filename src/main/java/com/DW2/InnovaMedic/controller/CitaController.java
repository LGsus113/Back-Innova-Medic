package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.ActualizarCitaCompletaDTO;
import com.DW2.InnovaMedic.dto.CitaRecetaVaciaDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.service.MaintenanceCita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PutMapping("/actualizar/informacion")
    public ResponseEntity<?> actualizarCitaCompleta(@RequestBody ActualizarCitaCompletaDTO actualizarCitaCompletaDTO) {
        try {
            maintenanceCita.actualizarCitaCompleta(
                    actualizarCitaCompletaDTO.id(),
                    actualizarCitaCompletaDTO.actionCitaMedicoDTO(),
                    actualizarCitaCompletaDTO.nombreMedico()
            );

            String estadoActualizado = maintenanceCita.actualizarEstadoCita(
                    actualizarCitaCompletaDTO.id(),
                    Cita.Estado.Finalizada
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", Map.of(
                            "message", "Datos actualizados y medicamentos agregados",
                            "estadoActualizacion", estadoActualizado
                    )
            ));
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Datos inválidos: " + ie.getMessage()
            ));
        } catch (IllegalStateException ise) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "error",
                    "message", "No se pudo actualizar el estado: " + ise.getMessage()
            ));
        } catch (ResponseStatusException rse) {
            assert rse.getReason() != null;

            return ResponseEntity.status(rse.getStatusCode()).body(Map.of(
                    "status", "error",
                    "message", rse.getReason()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error interno al procesar la solicitud: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/actualizar/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestParam Cita.Estado estado) {
        try {
            String estadoActualizado = maintenanceCita.actualizarEstadoCita(id, estado);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", estadoActualizado
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "Status", "error",
                    "message", "Error al actualizar estado: " + e.getMessage()
            ));
        }
    }
}
