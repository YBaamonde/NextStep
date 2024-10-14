package com.nextstep.nextstepBackEnd.jwt;

import com.nextstep.nextstepBackEnd.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET = "6qRMCxecb2htgVZQZfQX6XIqkg2ogwL0hsVSK9Akowk";

    public String generateToken(UserDetails usuario) {
        return generateToken(new HashMap<>(), usuario); // Llama al otro metodo con un mapa vacío.
    }

    // Metodo que genera los tokens
    public String generateToken(Map<String, Object> extraClaims, UserDetails usuario) {
        extraClaims.put("roles", ((Usuario) usuario).getRol().name()); // Agrega el rol al token

        return Jwts.builder()
                .setClaims(extraClaims) // Añade los claims personalizados, si los hay
                .setSubject(usuario.getUsername()) // Establece el nombre de usuario como sujeto
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de expiración
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }



    private Key getKey() {
        byte[] secretBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token)
    {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token)
    {
        return getExpiration(token).before(new Date());
    }
}
