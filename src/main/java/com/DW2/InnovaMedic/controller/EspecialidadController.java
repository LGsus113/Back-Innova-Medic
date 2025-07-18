package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.especialidades.ActualizarEspecialidadDTO;
import com.DW2.InnovaMedic.dto.especialidades.RegistrarEspecialidadDTO;
import com.DW2.InnovaMedic.dto.especialidades.ResumenEspecialidadParaAdminDTO;
import com.DW2.InnovaMedic.dto.especialidades.ResumenEspecialidadParaPacienteDTO;
import com.DW2.InnovaMedic.service.MaintenanceEspecialidades;
import com.DW2.InnovaMedic.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/especialidad")
public class EspecialidadController {
    private final MaintenanceEspecialidades maintenanceEspecialidades;

    @GetMapping("/lista-paciente")
    public ResponseEntity<?> listarEspecialidadesPaciente() {
        List<ResumenEspecialidadParaPacienteDTO> especialidades = maintenanceEspecialidades.listarEspecialidadesParaPaciente();
        return ResponseUtil.success(especialidades);
    }

    @GetMapping("/lista-admin")
    public ResponseEntity<?> listarEspecialidadesAdmin() {
        List<ResumenEspecialidadParaAdminDTO> especialidades = maintenanceEspecialidades.listarEspecialidadesAdmin();
        return ResponseUtil.success(especialidades);
    }

    @GetMapping("/contar-medicos/{idEspecialidad}")
    public ResponseEntity<?> cantidadMedicosAfiliadosEspecialidad(
            @PathVariable Integer idEspecialidad,
            @RequestParam String nombreEspecialidad
    ) {
        Integer cantidadMedicos = maintenanceEspecialidades.contarMedicosDeEspecialidad(idEspecialidad);
        return ResponseUtil.successMessage("Los medicos afiliados a " + nombreEspecialidad + " son " + cantidadMedicos);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarEspecialidad(@RequestBody RegistrarEspecialidadDTO registrarEspecialidadDTO) {
        maintenanceEspecialidades.registrarOReactivarEspecialidad(registrarEspecialidadDTO);
        return ResponseUtil.successMessage("Especialidad registrada correctamente");
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarEspecialidad(@RequestBody ActualizarEspecialidadDTO actualizarEspecialidadDTO) {
        maintenanceEspecialidades.actualizarEspecialidad(actualizarEspecialidadDTO);
        return ResponseUtil.successMessage("Especialidad actualizada correctamente");
    }

    @DeleteMapping("/eliminar/{idEspecialidad}")
    public ResponseEntity<?> eliminarEspecialidad(@PathVariable Integer idEspecialidad) {
        maintenanceEspecialidades.eliminarEspecialidadPorId(idEspecialidad);
        return ResponseUtil.successMessage("Especialidad eliminada correctamente");
    }
}
