package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.dto.LoginRequestDTO;
import com.DW2.InnovaMedic.dto.UsuarioDTO;
import com.DW2.InnovaMedic.service.MaintanceUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    MaintanceUsuario maintanceUsuario;

    @PostMapping("/log")
    public ResponseEntity<?> registrarMedico(@RequestBody LoginRequestDTO login) {
        try {
            UsuarioDTO usuarioDTO = maintanceUsuario.validarUsuario(login.email(), login.password());
            return ResponseEntity.ok(usuarioDTO);
        } catch (ResponseStatusException rse) {
            assert rse.getReason() != null;

            return ResponseEntity.status(rse.getStatusCode())
                    .body(Map.of(
                            "message", rse.getReason(),
                            "status", rse.getStatusCode().value()
                    ));
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor" + e.getMessage());
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.toString());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
