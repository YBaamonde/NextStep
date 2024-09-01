package com.nextstep.nextstepBackEnd;

import com.nextstep.nextstepBackEnd.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    @Mock
    private UserDetails userDetails; // Simula un objeto UserDetails para los tests

    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada test
        jwtService = new JwtService(); // Inicializa la instancia de JwtService
    }

    // Test para verificar la generación de un token JWT
    @Test
    public void testGetToken() {
        // Arrange: Configura el mock para devolver un nombre de usuario simulado
        String username = "testuser";
        when(userDetails.getUsername()).thenReturn(username);

        // Act: Genera el token usando el JwtService
        String token = jwtService.getToken(userDetails);

        // Assert: Verifica que el token no es nulo y no está vacío
        assertTrue(token != null && !token.isEmpty());
        verify(userDetails).getUsername(); // Verifica que se llamó al método getUsername
    }

    // Test para verificar la extracción del nombre de usuario desde un token JWT
    @Test
    public void testGetUsernameFromToken() {
        // Arrange: Configura el mock y genera un token para el usuario simulado
        String username = "testuser";
        when(userDetails.getUsername()).thenReturn(username);
        String token = jwtService.getToken(userDetails);

        // Act: Extrae el nombre de usuario desde el token generado
        String extractedUsername = jwtService.getUsernameFromToken(token);

        // Assert: Verifica que el nombre de usuario extraído coincide con el original
        assertEquals(username, extractedUsername);
    }

    // Test para verificar la validez de un token JWT
    @Test
    public void testIsTokenValid() {
        // Arrange: Configura el mock y genera un token para el usuario simulado
        String username = "testuser";
        when(userDetails.getUsername()).thenReturn(username);
        String token = jwtService.getToken(userDetails);

        // Act: Verifica la validez del token
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert: Verifica que el token es válido
        assertTrue(isValid);
    }
}
