package com.DW2.InnovaMedic.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseUtil {
    public static ResponseEntity<?> success(Object data) {
        return buildResponse(HttpStatus.OK, "success", Map.of("data", data));
    }

    public static ResponseEntity<?> successMessage(String message) {
        return buildResponse(HttpStatus.OK, "success", Map.of("message", message));
    }

    public static ResponseEntity<?> successWith(Map<String, Object> customMap) {
        return buildResponse(HttpStatus.OK, "success", customMap);
    }

    public static ResponseEntity<?> accepted(Map<String, Object> customMap) {
        return buildResponse(HttpStatus.ACCEPTED, "accepted", customMap);
    }

    public static ResponseEntity<?> error(HttpStatus status, String message) {
        return buildResponse(status, "error", Map.of("message", message));
    }

    private static ResponseEntity<?> buildResponse(HttpStatus status, String baseStatus, Map<String, Object> customContent) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", baseStatus);
        body.putAll(customContent);

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }
}
