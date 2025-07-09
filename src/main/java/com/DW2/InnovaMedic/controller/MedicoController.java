package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.registro.MedicoRegistroDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.service.MaintenanceMedico;
import com.DW2.InnovaMedic.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medicos")
public class MedicoController {
    private final MaintenanceMedico maintenanceMedico;

    @GetMapping("/cita/{id}")
    public ResponseEntity<?> listaCitasMedico(@PathVariable Integer id, @RequestParam(required = false) Cita.Estado estado, @RequestParam(required = false) String nombreUsuario) {
        List<CitaDTO> citasMedico = maintenanceMedico.obtenerCitasMedico(id, nombreUsuario, estado);

        if (citasMedico.isEmpty()) {
            return ResponseUtil.successWith(Map.of(
                    "message", "El médico existe, pero no tiene citas registradas.",
                    "idMedico", id
            ));
        }

        return ResponseUtil.success(citasMedico);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMedico(@RequestBody MedicoRegistroDTO medicoRegistroDTO) {
        maintenanceMedico.registrarMedicos(medicoRegistroDTO);
        return ResponseUtil.successMessage("Usuario registrado con éxito");
    }
}