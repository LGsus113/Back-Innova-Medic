package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.service.MaintenanceUsuario;
import com.DW2.InnovaMedic.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuario")
public class UsuarioController {
    private final MaintenanceUsuario maintenanceUsuario;

    @GetMapping("/{id}/perfil")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Integer id) {
        Object usuarioDTO = maintenanceUsuario.obtenerUsuarioPorId(id);
        return ResponseUtil.success(usuarioDTO);
    }
}