package com.DW2.InnovaMedic.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class SecurityEndpointRules {
    private static final String[] RUTAS_PUBLICAS = {
            "/login",
            "/error",
            "/api/pacientes/registrar",
            "/api/medicos/registrar",
            "/api/usuario/refresh"
    };

    private static final String[] RUTAS_MEDICO = {
            "/api/medicos/**",
            "/api/cita/receta/agregar-medicamento",
            "/api/cita/finalizar/informacion",
            "/api/cita/actualizar/info-cita",
            "/api/cita/actualizar/medicamento",
            "/api/cita/delete-medicamento/*"
    };

    private static final String[] RUTAS_PACIENTE = {
            "/api/pacientes/**",
            "/api/cita/disponibilidad",
            "/api/cita/registrar"
    };

    private static final String[] RUTAS_COMPARTIDAS = {
            "/api/usuario/**",
            "/api/cita/actualizar/*/estado",
            "/api/pdf/**"
    };

    public static void security(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
                .requestMatchers(RUTAS_PUBLICAS).permitAll()
                .requestMatchers(RUTAS_MEDICO).hasRole("Medico")
                .requestMatchers(RUTAS_PACIENTE).hasRole("Paciente")
                .requestMatchers(RUTAS_COMPARTIDAS).hasAnyRole("Medico", "Paciente")
                .anyRequest().authenticated();
    }
}
