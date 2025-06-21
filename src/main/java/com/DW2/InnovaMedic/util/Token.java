package com.DW2.InnovaMedic.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class Token {
    private final static String TOKEN_SECRETO = "QhwOQohWEdy6hrtEVpCR8IMJFgkSl57g";
    private final static Long ACCESS_TOKEN_DURACION = 15 * 60L;
    private final static Long REFRESH_TOKEN_DURACION = 86400L;

    public static String crearAccessToken(String user, String email, String rol) {
        return crearToken(user, email, ACCESS_TOKEN_DURACION, rol);
    }

    public static String crearRefreshToken(String user, String email, String rol) {
        return crearToken(user, email, REFRESH_TOKEN_DURACION, rol);
    }

    public static String crearToken(String user, String email, long duracion, String rol) {
        Date expiracionFecha = new Date(System.currentTimeMillis() + duracion * 1000L);

        return Jwts.builder()
                .setSubject(email)
                .claim("nombre", user)
                .claim("rol", rol)
                .setExpiration(expiracionFecha)
                .signWith(Keys.hmacShaKeyFor(TOKEN_SECRETO.getBytes()))
                .compact();
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
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(TOKEN_SECRETO.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String rol = (String) claims.get("rol");

            if (email == null || rol == null) return null;

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));
            return new UsernamePasswordAuthenticationToken(email, null, authorities);
        } catch (Exception e) {
            return null;
        }
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
            String rol = (String) claims.get("rol");

            Map<String, String> datos = new HashMap<>();
            datos.put("email", email);
            datos.put("nombre", nombre);
            datos.put("rol", rol);

            return datos;
        } catch (Exception e) {
            return null;
        }
    }
}
