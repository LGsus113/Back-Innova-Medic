package com.DW2.InnovaMedic.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseUtil {
    public static ResponseEntity<?> success(Object data) {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", data
        ));
    }

    public static ResponseEntity<?> successMessage(String message) {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", message
        ));
    }

    public static ResponseEntity<?> successWith(Map<String, Object> customMap) {
        return ResponseEntity.ok(customMap);
    }

    public static ResponseEntity<?> error(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON) // 👈 Añadir esto
                .body(Map.of(
                        "status", "error",
                        "message", message
                ));
    }

    public static ResponseEntity<?> errorWith(HttpStatus status, Map<String, Object> body) {
        return ResponseEntity.status(status).body(body);
    }
}
