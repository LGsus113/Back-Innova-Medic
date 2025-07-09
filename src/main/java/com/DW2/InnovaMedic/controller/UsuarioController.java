package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.service.MaintenanceUsuario;
import com.DW2.InnovaMedic.service.impl.UsuarioDetailImpl;
import com.DW2.InnovaMedic.service.impl.UsuarioServiceImpl;
import com.DW2.InnovaMedic.util.ResponseUtil;
import com.DW2.InnovaMedic.util.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuario")
public class UsuarioController {
    private final MaintenanceUsuario maintenanceUsuario;
    private final UsuarioServiceImpl usuarioService;

    @GetMapping("/{id}/perfil")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Integer id) {
        Object usuarioDTO = maintenanceUsuario.obtenerUsuarioPorId(id);
        return ResponseUtil.success(usuarioDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String email = Token.getEmailFromRefreshToken(refreshToken);

        if (email == null) {
            return ResponseEntity.status(401).body("Refresh token inválido o expirado");
        }

        UsuarioDetailImpl usuarioDetail = (UsuarioDetailImpl) usuarioService.loadUserByUsername(email);

        String nombre = usuarioDetail.getUser();
        String rol = usuarioDetail.getRole();

        String nuevoToken = Token.crearToken(nombre, email, rol);

        return ResponseEntity.ok(Map.of("token", nuevoToken));
    }
}