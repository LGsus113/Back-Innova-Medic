package com.DW2.InnovaMedic.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.io.IOException;
import java.util.*;

public class Token {
    private final static String TOKEN_SECRETO = "QhwOQohWEdy6hrtEVpCR8IMJFgkSl57g";
    private final static Long TOKEN_DURACION = 3_600L;

    public static String crearToken(String user, String email, String rol) {
        long expiracionTiempo = TOKEN_DURACION * 1_000L;
        Date expiracionFecha = new Date(System.currentTimeMillis() + expiracionTiempo);

        Map<String, Object> map = new HashMap<>();
        map.put("nombre", user);
        map.put("rol", rol);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expiracionFecha)
                .addClaims(map)
                .signWith(Keys.hmacShaKeyFor(TOKEN_SECRETO.getBytes()))
                .compact();
    }

    public static String crearRefreshToken(String email) {
        long duracion = 24 * 60 * 60 * 1000L;
        Date expiracion = new Date(System.currentTimeMillis() + duracion);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expiracion)
                .signWith(Keys.hmacShaKeyFor(TOKEN_SECRETO.getBytes()))
                .compact();
    }

    public static String getEmailFromRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(TOKEN_SECRETO.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public static UsernamePasswordAuthenticationToken getauth(String token, HttpServletResponse response) throws java.io.IOException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(TOKEN_SECRETO.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String rol = (String) claims.get("rol");

            if (email != null && rol != null) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol);
                return new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
            }

            return responderError(response, "Token sin email o rol");
        } catch (ExpiredJwtException e) {
            return responderError(response, "Token expirado");
        } catch (Exception e) {
            return responderError(response, "Token invalido o desconocido");
        }
    }

    private static UsernamePasswordAuthenticationToken responderError(HttpServletResponse response, String mensaje) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("""
                    {
                      "status": "error",
                      "message": "%s"
                    }
                """.formatted(mensaje));
        return null;
    }
}