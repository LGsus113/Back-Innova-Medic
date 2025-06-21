package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.entity.Usuario;
import com.DW2.InnovaMedic.service.MaintenanceUsuario;
import com.DW2.InnovaMedic.util.Token;
import com.DW2.InnovaMedic.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    @Autowired
    MaintenanceUsuario maintenanceUsuario;

    @GetMapping("/{id}/perfil")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Integer id) {
        try {
            Object usuarioDTO = maintenanceUsuario.obtenerUsuarioPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "user", usuarioDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "error",
                    "message", "Error al obtener el usuario: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refrescarToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || !Token.esValido(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token inválido o expirado");
        }

        Map<String, String> datos = Token.obtenerDatosDesdeRefreshToken(refreshToken);
        if (datos == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
        }

        String email = datos.get("email");
        String nombre = datos.get("nombre");

        Usuario usuario = maintenanceUsuario.buscarPorEmail(email)
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        String rol = UserUtil.role(usuario);

        String nuevoAccessToken = Token.crearAccessToken(nombre, email, rol);

        return ResponseEntity.ok(Map.of("accessToken", nuevoAccessToken));
    }
}
