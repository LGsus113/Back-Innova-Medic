package com.DW2.InnovaMedic.security;

import com.DW2.InnovaMedic.dto.UsuarioDTO;
import com.DW2.InnovaMedic.entity.Auth;
import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.entity.Usuario;
import com.DW2.InnovaMedic.service.impl.UsuarioDetailImpl;
import com.DW2.InnovaMedic.util.Token;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        Usuario usuario = userDetails.getUsuario();

        String token = Token.crearToken(userDetails.getUser(), userDetails.getUsername());

        String rol = "Desconocido";
        if (usuario instanceof Paciente) {
            rol = "Paciente";
        } else if (usuario instanceof Medico) {
            rol = "Medico";
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                rol
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = """
                {
                    "token": "%s",
                    "usuario": {
                        "idUsuario": %d,
                        "nombre": "%s",
                        "apellido": "%s",
                        "rol": "%s"
                    }
                }
                """.formatted(token, usuarioDTO.idUsuario(), usuarioDTO.nombre(), usuarioDTO.apellido(), usuarioDTO.rol());

        response.getWriter().write(jsonResponse);
    }
}
