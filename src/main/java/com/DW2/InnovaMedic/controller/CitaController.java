package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.*;
import com.DW2.InnovaMedic.dto.slot.SlotPorDiaDTO;
import com.DW2.InnovaMedic.dto.slot.SlotRequestDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.service.MaintenanceCita;
import com.DW2.InnovaMedic.service.MaintenanceDisponibilidadMedica;
import com.DW2.InnovaMedic.service.MaintenancePdfExportService;
import com.DW2.InnovaMedic.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cita")
public class CitaController {
    private final MaintenanceCita maintenanceCita;
    private final MaintenanceDisponibilidadMedica maintenanceDisponibilidadMedica;
    private final MaintenancePdfExportService maintenancePdfExportService;

    @GetMapping("/{idCita}/receta-pdf")
    public ResponseEntity<?> descargarPDF(@PathVariable Integer idCita) {
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
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<?> obtenerSlotsDisponibles(
            @RequestParam("idMedico") Integer idMedico,
            @RequestParam("fechaInicio") String fechaInicioStr,
            @RequestParam("fechaFin") String fechaFinStr
    ) {
        LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
        LocalDate fechaFin = LocalDate.parse(fechaFinStr);

        SlotRequestDTO slotRequestDTO = new SlotRequestDTO(idMedico, fechaInicio, fechaFin);

        List<SlotPorDiaDTO> slots = maintenanceDisponibilidadMedica.obtenerSlotsDisponibles(slotRequestDTO);

        if (slots.isEmpty()) {
            return ResponseUtil.successMessage("No existen slots disponibles");
        }

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", slots,
                "message", "Slots cargados correctamente"
        ));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCitaConRecetaVacia(@RequestBody CitaRecetaVaciaDTO citaRecetaVaciaDTO) {
        Integer idCita = maintenanceCita.registrarCitaVacia(citaRecetaVaciaDTO);
        return ResponseUtil.success(Map.of("idCita", idCita));
    }

    @PostMapping("/receta/agregar-medicamento")
    public ResponseEntity<?> agregarMedicamentosReceta(@RequestBody AgregarMedicamentoRecetaDTO medicamentoRecetaDTO) {
        maintenanceCita.agregarMedicamento(medicamentoRecetaDTO.idCita(), medicamentoRecetaDTO.listaMedicamentos());
        return ResponseUtil.successMessage("Medicamentos agregados correctamente");
    }

    @PutMapping("/actualizar/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestParam Cita.Estado estado) {
        String estadoActualizado = maintenanceCita.actualizarEstadoCita(id, estado);
        return ResponseUtil.success(Map.of("estado", estadoActualizado));
    }

    @PutMapping("/finalizar/informacion")
    public ResponseEntity<?> terminarRegistroCitaCompleta(@RequestBody ActualizarCitaCompletaDTO actualizarCitaCompletaDTO) {
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
    }

    @PutMapping("/actualizar/info-cita")
    public ResponseEntity<?> actualizarInformacionCita(@RequestBody ActualizarCitaCompletaDTO actualizarCitaCompletaDTO) {
        maintenanceCita.actualizarInformacionMedicaCita(actualizarCitaCompletaDTO.id(), actualizarCitaCompletaDTO.actionCitaMedicoDTO());
        return ResponseUtil.successMessage("Datos actualizados");
    }

    @PutMapping("/actualizar/medicamento")
    public ResponseEntity<?> actualizarMedicamento(@RequestBody MedicamentoRecetaDTO medicamentoRecetaDTO) {
        maintenanceCita.actualizarMedicamento(medicamentoRecetaDTO);
        return ResponseUtil.successMessage("Medicamento actualizado");
    }

    @DeleteMapping("/delete-medicamento/{id}")
    public ResponseEntity<?> eliminarMedicamento(@PathVariable Integer id) {
        maintenanceCita.eliminarMedicamento(id);
        return ResponseUtil.successMessage("Medicamento eliminado");
    }
}