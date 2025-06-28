package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.service.MaintenanceUsuario;
import com.DW2.InnovaMedic.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    @Autowired
    MaintenanceUsuario maintenanceUsuario;

    @GetMapping("/{id}/perfil")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Integer id) {
        try {
            Object usuarioDTO = maintenanceUsuario.obtenerUsuarioPorId(id);
            return ResponseUtil.success(usuarioDTO);
        } catch (IllegalArgumentException e) {
            return ResponseUtil.error(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + e.getMessage());
        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, "Error al obtener el usuario: " + e.getMessage());
        }
    }
}