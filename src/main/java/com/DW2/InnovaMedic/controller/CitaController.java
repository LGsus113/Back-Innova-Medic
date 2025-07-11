package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.*;
import com.DW2.InnovaMedic.dto.slot.SlotPorDiaDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.service.MaintenanceCita;
import com.DW2.InnovaMedic.service.MaintenanceDisponibilidadMedica;
import com.DW2.InnovaMedic.service.MaintenancePdfExportService;
import com.DW2.InnovaMedic.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cita")
public class CitaController {
    private final MaintenanceCita maintenanceCita;
    private final MaintenanceDisponibilidadMedica maintenanceDisponibilidadMedica;
    private final MaintenancePdfExportService maintenancePdfExportService;

    @GetMapping("/disponibilidad")
    public ResponseEntity<?> obtenerSlotsDisponibles(
            @RequestParam("idMedico") Integer idMedico,
            @RequestParam("fechaInicio") String fechaInicioStr,
            @RequestParam("fechaFin") String fechaFinStr
    ) {
        List<SlotPorDiaDTO> slots = maintenanceDisponibilidadMedica.obtenerSlotsDisponibles(idMedico, fechaInicioStr, fechaFinStr);

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
        Integer idCita = medicamentoRecetaDTO.idCita();
        maintenancePdfExportService.eliminarArchivo(idCita);

        maintenanceCita.agregarMedicamento(medicamentoRecetaDTO.idCita(), medicamentoRecetaDTO.listaMedicamentos());

        maintenancePdfExportService.generarPDFAsync(idCita);

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

        try {
            maintenancePdfExportService.generarPDFAsync(actualizarCitaCompletaDTO.id());
        } catch (Exception e) {
            throw new IllegalStateException("Error al inicar la generacion del PDF: " + e.getMessage());
        }

        return ResponseUtil.success(Map.of(
                "message", "Datos actualizados y medicamentos agregados",
                "estadoActualizacion", estadoActualizado
        ));
    }

    @PutMapping("/actualizar/info-cita")
    public ResponseEntity<?> actualizarInformacionCita(@RequestBody ActualizarCitaCompletaDTO actualizarCitaCompletaDTO) {
        Integer idCita = actualizarCitaCompletaDTO.id();
        maintenancePdfExportService.eliminarArchivo(idCita);

        maintenanceCita.actualizarInformacionMedicaCita(actualizarCitaCompletaDTO.id(), actualizarCitaCompletaDTO.actionCitaMedicoDTO());

        maintenancePdfExportService.generarPDFAsync(idCita);

        return ResponseUtil.successMessage("Datos actualizados");
    }

    @PutMapping("/actualizar/medicamento/{idCita}")
    public ResponseEntity<?> actualizarMedicamento(@PathVariable Integer idCita, @RequestBody MedicamentoRecetaDTO medicamentoRecetaDTO) {
        maintenancePdfExportService.eliminarArchivo(idCita);

        maintenanceCita.actualizarMedicamento(medicamentoRecetaDTO);

        maintenancePdfExportService.generarPDFAsync(idCita);

        return ResponseUtil.successMessage("Medicamento actualizado");
    }

    @DeleteMapping("/delete-medicamento/{idCita}/{id}")
    public ResponseEntity<?> eliminarMedicamento(@PathVariable Integer idCita, @PathVariable Integer id) {
        maintenancePdfExportService.eliminarArchivo(idCita);

        maintenanceCita.eliminarMedicamento(id);

        maintenancePdfExportService.generarPDFAsync(idCita);

        return ResponseUtil.successMessage("Medicamento eliminado");
    }
}