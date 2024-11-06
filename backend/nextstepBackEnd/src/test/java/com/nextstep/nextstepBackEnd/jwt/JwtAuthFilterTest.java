package com.nextstep.nextstepBackEnd.jwt;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    // Configuración antes de cada test
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();  // Limpia el contexto de seguridad antes de cada test
    }

    // Test para autenticar un usuario con un token válido
    @Test
    public void shouldAuthenticateWithValidToken() throws IOException, ServletException {
        // Arrange: Configura las respuestas simuladas para un token válido
        String token = "validToken";
        String username = "user";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token); // Simula el header con el token
        when(jwtService.getUsernameFromToken(anyString())).thenReturn(username);  // Simula la extracción del nombre de usuario desde el token
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);  // Simula la carga del usuario
        when(jwtService.isTokenValid(eq(token), eq(userDetails))).thenReturn(true);  // Simula la validación del token
        when(jwtService.getRolesFromToken(token)).thenReturn(Collections.singletonList("ROLE_USER"));  // Simula la extracción de roles

        // Act: Ejecuta el filtro
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert: Verifica que el proceso se realizó correctamente
        verify(jwtService).getUsernameFromToken(token);
        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtService).isTokenValid(eq(token), eq(userDetails));
        verify(jwtService).getRolesFromToken(token);
        verify(filterChain).doFilter(request, response);

        // Verifica que el contexto de autenticación fue actualizado
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);  // Verifica que la autenticación no sea nula
        assertEquals(userDetails, auth.getPrincipal());  // Verifica que el usuario autenticado es el correcto
    }

    // Test para simular un token inválido y verificar que no se autentica
    @Test
    public void shouldNotAuthenticateWithInvalidToken() throws Exception {
        // Arrange: Configura un token inválido y otros mocks necesarios
        String token = "invalidToken";
        String username = "testuser";

        // Configura los mocks para un token inválido y usuario no encontrado
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.getUsernameFromToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(null); // Usuario no encontrado
        when(jwtService.isTokenValid(token, null)).thenReturn(false); // Token inválido

        // Asegura que el contexto está limpio antes de iniciar el test
        SecurityContextHolder.clearContext();

        // Act: Ejecuta el filtro
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert: Verifica que el filtro continúe y que el contexto de autenticación permanezca vacío
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(), "Authentication context should be null for invalid token");
    }





    // Test para verificar que el filtro continúa si no hay token en el request
    @Test
    public void shouldContinueWithoutToken() throws IOException, ServletException {
        // Arrange: Simula que no hay token en el request
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        // Act: Ejecuta el filtro
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert: Verifica que el filtro continúa sin establecer autenticación
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());  // Verifica que no hay autenticación
    }
}
