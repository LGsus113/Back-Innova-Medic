package com.DW2.InnovaMedic.security;

import com.DW2.InnovaMedic.entity.Auth;
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
            auth = new ObjectMapper().readValue(request.getReader(), Auth.class );
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

        String token = Token.crearToken(userDetails.getUser(), userDetails.getUsername());

        response.addHeader("Authorization", "Bearer " + token);

        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
