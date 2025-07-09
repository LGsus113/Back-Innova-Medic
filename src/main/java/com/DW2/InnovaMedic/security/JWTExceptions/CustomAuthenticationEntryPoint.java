package com.DW2.InnovaMedic.security.JWTExceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String message = "Credenciales inválidas";
        if (authException.getCause() instanceof UsernameNotFoundException usernameEx) {
            message = usernameEx.getMessage();
        } else if (authException.getMessage() != null && !authException.getMessage().isBlank()) {
            message = authException.getMessage();
        }

        Map<String, Object> body = new HashMap<>();
        body.put("status", 401);
        body.put("message", message);

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
