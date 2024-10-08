package com.nextstep.nextstepBackEnd.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @Mock
    private AuthService authService;  // Servicio simulado para pruebas

    @InjectMocks
    private AuthController authController;  // Controlador que se está probando

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks antes de cada prueba
    }

    @Test
    public void testLogin() {
        // Arrange
        String token = "testtoken";
        LoginRequest request = new LoginRequest("testuser", "testpassword");
        AuthResponse authResponse = new AuthResponse(token);

        // Configura el comportamiento simulado del servicio
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);  // Verifica que el código de estado sea 200 OK
        assertThat(response.getBody()).isNotNull();  // Verifica que el cuerpo de la respuesta no sea nulo
        assertThat(response.getBody().getToken()).isEqualTo(token);  // Verifica que el token en la respuesta sea el esperado
    }

    @Test
    public void testRegister() {
        // Arrange
        String token = "testtoken";
        RegisterRequest request = new RegisterRequest("Test User", "testuser", "testpassword", "normal");
        AuthResponse authResponse = new AuthResponse(token);

        // Configura el comportamiento simulado del servicio
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.register(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);  // Verifica que el código de estado sea 200 OK
        assertThat(response.getBody()).isNotNull();  // Verifica que el cuerpo de la respuesta no sea nulo
        assertThat(response.getBody().getToken()).isEqualTo(token);  // Verifica que el token en la respuesta sea el esperado
    }

    // Prueba para casos en los que el servicio lanza una excepción
    @Test
    public void testLoginThrowsException() {
        LoginRequest request = new LoginRequest("username", "password");

        // Simula que el servicio lanza una excepción
        when(authService.login(request)).thenThrow(new RuntimeException("Error en el servicio"));

        ResponseEntity<AuthResponse> response = authController.login(request);

        // Verifica que el código de estado sea 500
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testRegisterThrowsException() {
        // Proporciona todos los parámetros requeridos
        RegisterRequest request = new RegisterRequest("Nombre", "username", "password", "rol");

        // Simula que el servicio lanza una excepción
        when(authService.register(request)).thenThrow(new RuntimeException("Error en el servicio"));

        ResponseEntity<AuthResponse> response = authController.register(request);

        // Verifica que el código de estado sea 500
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}
