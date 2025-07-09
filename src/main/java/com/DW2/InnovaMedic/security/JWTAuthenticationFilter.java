package com.DW2.InnovaMedic.security;

import com.DW2.InnovaMedic.entity.Auth;
import com.DW2.InnovaMedic.service.impl.UsuarioDetailImpl;
import com.DW2.InnovaMedic.util.Token;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        Auth auth = new Auth();

        try {
            auth = new ObjectMapper().readValue(request.getReader(), Auth.class);
        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken userPat = new UsernamePasswordAuthenticationToken(
                auth.getEmail(),
                auth.getContrasenia(),
                Collections.emptyList());

        return getAuthenticationManager().authenticate(userPat);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UsuarioDetailImpl userDetails = (UsuarioDetailImpl) authResult.getPrincipal();
        String token = Token.crearToken(userDetails.getUser(), userDetails.getUsername(), userDetails.getRole());
        String refreshToken = Token.crearRefreshToken(userDetails.getUsername());

        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("idUsuario", userDetails.getIdUser());
        usuarioMap.put("nombre", userDetails.getUser());
        usuarioMap.put("apellido", userDetails.getLastnameUser());
        usuarioMap.put("rol", userDetails.getRole());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("refreshToken", refreshToken);
        responseBody.put("usuario", usuarioMap);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String mensaje = "Credenciales inválidas";
        if (failed.getCause() instanceof UsernameNotFoundException usernameEx) {
            mensaje = usernameEx.getMessage();
        } else if (failed.getMessage() != null && !failed.getMessage().isBlank()) {
            mensaje = failed.getMessage();
        }

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("status", 401);
        errorBody.put("message", mensaje);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), errorBody);
    }
}