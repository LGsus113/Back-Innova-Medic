package com.DW2.InnovaMedic.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class SecurityEndpointRules {
    public static void security(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
                .requestMatchers("/login", "/api/pacientes/registrar", "/api/medicos/registrar").permitAll()
                .requestMatchers("/api/medicos/**", "/api/cita/receta/agregar-medicamento", "/api/cita/actualizar/informacion", "/api/cita/actualizar/*/estado", "/api/cita/update/info-cita", "/api/cita/update/medicamento", "/api/cita/delete-medicamento/*").hasRole("Medico")
                .requestMatchers("/api/pacientes/**", "/api/cita/disponibilidad", "/api/cita/registrar").hasRole("Paciente")
                .requestMatchers("/api/usuario/**", "/api/cita/*/receta-pdf").hasAnyRole("Medico", "Paciente")
                .anyRequest().authenticated();
    }
}
