package com.DW2.InnovaMedic.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class SecurityEndpointRules {
    public static void security(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
                .requestMatchers("/login", "/api/pacientes/registrar", "/api/medicos/registrar").permitAll()
                .requestMatchers("/api/medicos/**", "/api/cita/actualizar/informacion", "/api/cita/actualizar/*/estado").hasRole("Medico")
                .requestMatchers("/api/pacientes/**", "/api/cita/disponibilidad", "/api/cita/registrar").hasRole("Paciente")
                .requestMatchers("/api/usuario/**").hasAnyRole("Medico", "Paciente")
                .anyRequest().authenticated();
    }
}
