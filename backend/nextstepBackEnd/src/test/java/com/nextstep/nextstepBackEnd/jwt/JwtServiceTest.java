package com.nextstep.nextstepBackEnd.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private UserDetails userDetails;
    private String token;

    @BeforeEach
    public void setUp() {
        // Crear un UserDetails real
        userDetails = new User("testuser", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        // Generar el token
        token = jwtService.generateToken(userDetails);
    }

    // Test para verificar que el token contiene el username correcto
    @Test
    public void shouldExtractUsernameFromToken() {
        String username = jwtService.getUsernameFromToken(token);
        assertEquals("testuser", username);
    }

    // Test para validar el token generado
    @Test
    public void shouldValidateToken() {
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    // Test para extraer roles del token
    @Test
    public void shouldExtractRolesFromToken() {
        List<String> roles = jwtService.getRolesFromToken(token);
        assertEquals(1, roles.size());
        assertEquals("ROLE_USER", roles.get(0));
    }
}
