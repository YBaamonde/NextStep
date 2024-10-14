package com.nextstep.nextstepBackEnd.jwt;

import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class JwtServiceTest {

    @Mock
    private Usuario usuario; // Simula un objeto Usuario directamente

    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada test
        jwtService = new JwtService(); // Inicializa la instancia de JwtService
    }

    // Test para verificar la generación de un token JWT
    @Test
    public void testGenerateToken() {
        // Arrange: Configura el mock para devolver un nombre de usuario simulado
        String username = "testuser";
        when(usuario.getUsername()).thenReturn(username);

        // Act: Genera el token usando el JwtService
        String token = jwtService.generateToken(usuario);

        // Assert: Verifica que el token no es nulo y no está vacío
        assertNotNull(token);
        assertFalse(token.isEmpty());
        verify(usuario).getUsername(); // Verifica que se llamó al método getUsername
    }

    // Test para verificar la extracción del nombre de usuario desde un token JWT
    @Test
    public void testGetUsernameFromToken() {
        // Arrange: Configura el mock y genera un token para el usuario simulado
        String username = "testuser";
        when(usuario.getUsername()).thenReturn(username);
        String token = jwtService.generateToken(usuario);

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
        when(usuario.getUsername()).thenReturn(username);
        String token = jwtService.generateToken(usuario);

        // Act: Verifica la validez del token
        boolean isValid = jwtService.isTokenValid(token, usuario);

        // Assert: Verifica que el token es válido
        assertTrue(isValid);
    }


    // Test para verificar la obtención del rol desde un token
    @Test
    public void testGetRolesFromToken() {
        // Arrange: Configura un usuario simulado con un rol específico
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setRol(Rol.admin); // El usuario tiene el rol admin

        // Genera un token para este usuario
        String token = jwtService.generateToken(usuario);

        // Act: Extrae los roles desde el token
        List<String> roles = jwtService.getRolesFromToken(token);

        // Assert: Verifica que el rol extraído es el correcto
        assertNotNull(roles); // Verifica que los roles no sean nulos
        assertFalse(roles.isEmpty()); // Verifica que los roles no estén vacíos
        assertEquals(1, roles.size()); // Verifica que solo haya un rol
        assertEquals("admin", roles.get(0)); // Verifica que el rol sea admin
    }

}
