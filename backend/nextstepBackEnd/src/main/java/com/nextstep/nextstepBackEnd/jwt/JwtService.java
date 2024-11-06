package com.nextstep.nextstepBackEnd.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;

    // Metodo que genera los tokens (sustituyó al metodo antiguo para usar `getKey()` en la firma)
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(getKey())
                .compact();
    }


    // Metodo que obtiene la clave secreta codificada (sustituyó al uso directo de `SECRET` por seguridad)
    private Key getKey() {
        byte[] secretBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    // Obtener el nombre de usuario desde el token
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Validar si el token es válido
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Obtener todos los claims (sustituyó a `parser().setSigningKey(SECRET)` por `parserBuilder()` y `getKey()`)
    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey()) // Usamos `getKey()` para obtener la clave segura
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Obtener un claim específico
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Obtener la fecha de expiración del token
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    // Extraer los roles desde el token
    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("roles", List.class); // Extrae los roles como una lista desde el token
    }

    // Métodos antiguos:
    // `signWith(SignatureAlgorithm.HS256, SECRET)` en `generateToken`
    // - Reemplazado por `signWith(getKey())` para mejorar la seguridad usando una clave HMAC.
    //
    // `parser().setSigningKey(SECRET)` en `getAllClaims`
    // - Reemplazado por `parserBuilder().setSigningKey(getKey())` para mayor seguridad en la clave.
}
