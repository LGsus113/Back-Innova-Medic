package com.DW2.InnovaMedic.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Token {
    private final static String TOKEN_SECRETO = "QhwOQohWEdy6hrtEVpCR8IMJFgkSl57g";
    private final static Long ACCESS_TOKEN_DURACION = 15 * 60L;
    private final static Long REFRESH_TOKEN_DURACION = 86400L;

    public static String crearAccessToken(String user, String email) {
        return crearToken(user, email, ACCESS_TOKEN_DURACION);
    }

    public static String crearRefreshToken(String user, String email) {
        return crearToken(user, email, REFRESH_TOKEN_DURACION);
    }

    public static String crearToken(String user, String email, long duracion) {
        Date expiracionFecha = new Date(System.currentTimeMillis() + duracion * 1000L);

        Map<String, Object> map = new HashMap<>();
        map.put("nombre", user);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expiracionFecha)
                .addClaims(map)
                .signWith(Keys.hmacShaKeyFor(TOKEN_SECRETO.getBytes()))
                .compact();
    }

    public static String getEmailDesdeToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(TOKEN_SECRETO.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean esValido(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(TOKEN_SECRETO.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static UsernamePasswordAuthenticationToken getauth(String token) {
        String email = getEmailDesdeToken(token);

        return email != null
                ? new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList())
                : null;
    }

    public static Map<String, String> obtenerDatosDesdeRefreshToken(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(TOKEN_SECRETO.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String nombre = (String) claims.get("nombre");

            Map<String, String> datos = new HashMap<>();
            datos.put("email", email);
            datos.put("nombre", nombre);

            return datos;
        } catch (Exception e) {
            return null;
        }
    }
}
