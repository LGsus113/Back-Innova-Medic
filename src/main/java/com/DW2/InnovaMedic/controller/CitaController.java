package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.*;
import com.DW2.InnovaMedic.dto.slot.SlotPorDiaDTO;
import com.DW2.InnovaMedic.dto.slot.SlotRequestDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.service.MaintenanceCita;
import com.DW2.InnovaMedic.service.MaintenanceDisponibilidadMedica;
import com.DW2.InnovaMedic.service.MaintenancePdfExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cita")
public class CitaController {
    @Autowired
    MaintenanceCita maintenanceCita;

    @Autowired
    MaintenanceDisponibilidadMedica maintenanceDisponibilidadMedica;

    @Autowired
    MaintenancePdfExportService maintenancePdfExportService;

    @GetMapping("/{idCita}/receta-pdf")
    public ResponseEntity<?> descargarPDF(@PathVariable Integer idCita) {
        try {
            CitaDTO citaDTO = maintenanceCita.obtenerCitaCompletaPorID(idCita);
            RecetaDTO recetaDTO = citaDTO.recetaDTO();

            if (recetaDTO == null || recetaDTO.medicamentos() == null || recetaDTO.medicamentos().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Map.of(
                                "status", HttpStatus.BAD_REQUEST.value(),
                                "message", "La cita con ID " + idCita + " no tiene una receta asociada"
                        )
                );
            }

            byte[] pdf = maintenancePdfExportService.exportarRecetaComoPDF(citaDTO);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=receta-" + idCita + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "message", "Error inesperado al generar el PDF: " + e.getMessage()
                    )
            );
        }
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<?> obtenerSlotsDisponibles(
            @RequestParam("idMedico") Integer idMedico,
            @RequestParam("fechaInicio") String fechaInicioStr,
            @RequestParam("fechaFin") String fechaFinStr
    ) {
        try {
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);

            SlotRequestDTO slotRequestDTO = new SlotRequestDTO(idMedico, fechaInicio, fechaFin);

            List<SlotPorDiaDTO> slots = maintenanceDisponibilidadMedica.obtenerSlotsDisponibles(slotRequestDTO);

            if (slots.isEmpty()) {
                return ResponseEntity.ok().body(
                        Map.of(
                                "status", HttpStatus.OK.value(),
                                "message", "No existen slots disponibles"
                        )
                );
            }

            return ResponseEntity.ok(slots);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "status", HttpStatus.NOT_FOUND.value(),
                            "error", "Problemas encontrados.",
                            "message", ie.getMessage()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error", "Error interno al buscar citas",
                            "message", e.getMessage()
                    )
            );
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCitaConRecetaVacia(@RequestBody CitaRecetaVaciaDTO citaRecetaVaciaDTO) {
        try {
            Integer idCita = maintenanceCita.registrarCitaVacia(citaRecetaVaciaDTO);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "idCita", idCita
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

    @PostMapping("/receta/agregar-medicamento")
    public ResponseEntity<?> agregarMedicamentosReceta(@RequestBody AgregarMedicamentoRecetaDTO medicamentoRecetaDTO) {
        try {
            maintenanceCita.agregarMedicamento(medicamentoRecetaDTO.idCita(), medicamentoRecetaDTO.listaMedicamentos());

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Medicamentos agregados correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Error al agregar medicamentos: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/actualizar/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestParam Cita.Estado estado) {
        try {
            String estadoActualizado = maintenanceCita.actualizarEstadoCita(id, estado);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "estado", estadoActualizado
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "Status", "error",
                    "message", "Error al actualizar estado: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/finalizar/informacion")
    public ResponseEntity<?> terminarRegistroCitaCompleta(@RequestBody ActualizarCitaCompletaDTO actualizarCitaCompletaDTO) {
        try {
            maintenanceCita.terminarDeRegistrarCitaCompleta(
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

    @PutMapping("/actualizar/info-cita")
    public ResponseEntity<?> actualizarInformacionCita(@RequestBody ActualizarCitaCompletaDTO actualizarCitaCompletaDTO) {
        try {
            maintenanceCita.actualizarInformacionMedicaCita(actualizarCitaCompletaDTO.id(), actualizarCitaCompletaDTO.actionCitaMedicoDTO());

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Datos actualizados"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error interno al editar la informacion de la cita: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/actualizar/medicamento")
    public ResponseEntity<?> actualizarMedicamento(@RequestBody MedicamentoRecetaDTO medicamentoRecetaDTO) {
        try {
            maintenanceCita.actualizarMedicamento(medicamentoRecetaDTO);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Medicamento actualizado"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error interno al editar el medicamento: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/delete-medicamento/{id}")
    public ResponseEntity<?> eliminarMedicamento(@PathVariable Integer id) {
        try {
            maintenanceCita.eliminarMedicamento(id);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Medicamento eliminado"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "Status", "error",
                    "message", "Error al eliminar medicamento: " + e.getMessage()
            ));
        }
    }
}