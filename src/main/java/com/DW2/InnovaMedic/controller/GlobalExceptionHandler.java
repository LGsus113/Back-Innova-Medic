package com.DW2.InnovaMedic.controller;

import com.DW2.InnovaMedic.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException iaex) {
        return ResponseUtil.error(HttpStatus.BAD_REQUEST, iaex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalState(IllegalStateException isex) {
        return ResponseUtil.error(HttpStatus.NOT_FOUND, isex.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatus(ResponseStatusException rsex) {
        HttpStatus status = (rsex.getStatusCode() instanceof HttpStatus)
                ? (HttpStatus) rsex.getStatusCode()
                : HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseUtil.error(status, rsex.getReason());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException manvex) {
        Map<String, String> errores = new HashMap<>();
        manvex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));

        return ResponseUtil.error(HttpStatus.BAD_REQUEST, "Errores de validación: " + errores);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno: " + ex.getMessage());
    }
}