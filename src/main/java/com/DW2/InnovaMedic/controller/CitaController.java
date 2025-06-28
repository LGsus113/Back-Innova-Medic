package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.*;
import com.DW2.InnovaMedic.dto.slot.SlotPorDiaDTO;
import com.DW2.InnovaMedic.dto.slot.SlotRequestDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.service.MaintenanceCita;
import com.DW2.InnovaMedic.service.MaintenanceDisponibilidadMedica;
import com.DW2.InnovaMedic.service.MaintenancePdfExportService;
import com.DW2.InnovaMedic.util.ResponseUtil;
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
                return ResponseUtil.error(HttpStatus.BAD_REQUEST, "La cita con ID " + idCita + " no tiene una receta asociada");
            }

            byte[] pdf = maintenancePdfExportService.exportarRecetaComoPDF(citaDTO);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=receta-" + idCita + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado al generar el PDF: " + e.getMessage());
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
                return ResponseUtil.successMessage("No existen slots disponibles");
            }

            return ResponseEntity.ok(slots);
        } catch (IllegalArgumentException ie) {
            return ResponseUtil.error(HttpStatus.NOT_FOUND, ie.getMessage());
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al buscar citas: " + e.getMessage());
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCitaConRecetaVacia(@RequestBody CitaRecetaVaciaDTO citaRecetaVaciaDTO) {
        try {
            Integer idCita = maintenanceCita.registrarCitaVacia(citaRecetaVaciaDTO);
            return ResponseUtil.successWith(Map.of(
                    "status", "success",
                    "idCita", idCita
            ));
        } catch (IllegalArgumentException ie) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, ie.getMessage());
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al registrar cita: " + e.getMessage());
        }
    }

    @PostMapping("/receta/agregar-medicamento")
    public ResponseEntity<?> agregarMedicamentosReceta(@RequestBody AgregarMedicamentoRecetaDTO medicamentoRecetaDTO) {
        try {
            maintenanceCita.agregarMedicamento(medicamentoRecetaDTO.idCita(), medicamentoRecetaDTO.listaMedicamentos());

            return ResponseUtil.successMessage("Medicamentos agregados correctamente");
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, "Error al agregar medicamentos: " + e.getMessage());
        }
    }

    @PutMapping("/actualizar/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestParam Cita.Estado estado) {
        try {
            String estadoActualizado = maintenanceCita.actualizarEstadoCita(id, estado);

            return ResponseUtil.success(Map.of("estado", estadoActualizado));
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, "Error al actualizar estado: " + e.getMessage());
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

            return ResponseUtil.success(Map.of(
                    "message", "Datos actualizados y medicamentos agregados",
                    "estadoActualizacion", estadoActualizado
            ));
        } catch (IllegalArgumentException ie) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, "Datos inválidos: " + ie.getMessage());
        } catch (IllegalStateException ise) {
            return ResponseUtil.error(HttpStatus.CONFLICT, "No se pudo actualizar el estado: " + ise.getMessage());
        } catch (ResponseStatusException rse) {
            assert rse.getReason() != null;
            return ResponseUtil.error(HttpStatus.CONFLICT, "No se pudo actualizar el estado: " + rse.getMessage());
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al procesar la solicitud: " + e.getMessage());
        }
    }

    @PutMapping("/actualizar/info-cita")
    public ResponseEntity<?> actualizarInformacionCita(@RequestBody ActualizarCitaCompletaDTO actualizarCitaCompletaDTO) {
        try {
            maintenanceCita.actualizarInformacionMedicaCita(actualizarCitaCompletaDTO.id(), actualizarCitaCompletaDTO.actionCitaMedicoDTO());
            return ResponseUtil.successMessage("Datos actualizados");
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al editar la informacion de la cita: " + e.getMessage());
        }
    }

    @PutMapping("/actualizar/medicamento")
    public ResponseEntity<?> actualizarMedicamento(@RequestBody MedicamentoRecetaDTO medicamentoRecetaDTO) {
        try {
            maintenanceCita.actualizarMedicamento(medicamentoRecetaDTO);
            return ResponseUtil.successMessage("Medicamento actualizado");
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al editar el medicamento: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-medicamento/{id}")
    public ResponseEntity<?> eliminarMedicamento(@PathVariable Integer id) {
        try {
            maintenanceCita.eliminarMedicamento(id);
            return ResponseUtil.successMessage("Medicamento eliminado");
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, "Error al eliminar medicamento: " + e.getMessage());
        }
    }
}