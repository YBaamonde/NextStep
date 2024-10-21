package com.nextstep.nextstepBackEnd.auth;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.nextstep.nextstepBackEnd.controller.AuthController;
import com.nextstep.nextstepBackEnd.exception.InvalidCredentialsException;
import com.nextstep.nextstepBackEnd.exception.UserAlreadyExistsException;
import com.nextstep.nextstepBackEnd.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthControllerTest {

    @Mock
    private AuthService authService; // Servicio simulado

    @InjectMocks
    private AuthController authController; // Controlador que se está probando

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada prueba
    }

    @Test
    public void testLoginSuccess() {
        // Arrange
        String token = "testtoken";
        LoginRequest request = new LoginRequest("testuser", "testemail@example.com", "testpassword");
        AuthResponse authResponse = new AuthResponse(token);

        // Simula el comportamiento del servicio
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isEqualTo(token);
    }

    @Test
    public void testLoginInvalidCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest("wronguser", "wrongemail@example.com", "wrongpassword");

        // Simula que el servicio lanza InvalidCredentialsException
        when(authService.login(any(LoginRequest.class))).thenThrow(new InvalidCredentialsException("Credenciales inválidas"));

        // Act
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNull(); // No debería haber cuerpo en caso de error 401
    }

    @Test
    public void testLoginUnhandledException() {
        // Arrange
        LoginRequest request = new LoginRequest("testuser", "testemail@example.com", "testpassword");

        // Simula que el servicio lanza una excepción no controlada
        when(authService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("Error interno"));

        // Act
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull(); // No debería haber cuerpo en caso de error 500
    }

    @Test
    public void testRegisterSuccess() {
        // Arrange
        String token = "testtoken";
        RegisterRequest request = new RegisterRequest("testuser", "testemail@example.com", "testpassword");
        AuthResponse authResponse = new AuthResponse(token);

        // Simula el comportamiento del servicio
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.register(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isEqualTo(token);
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest("existinguser", "existingemail@example.com", "password");

        // Simula que el servicio lanza UserAlreadyExistsException
        when(authService.register(any(RegisterRequest.class))).thenThrow(new UserAlreadyExistsException("Usuario ya existe"));

        // Act
        ResponseEntity<AuthResponse> response = authController.register(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNull(); // No debería haber cuerpo en caso de error 409
    }

    @Test
    public void testRegisterUnhandledException() {
        // Arrange
        RegisterRequest request = new RegisterRequest("testuser", "testemail@example.com", "testpassword");

        // Simula que el servicio lanza una excepción no controlada
        when(authService.register(any(RegisterRequest.class))).thenThrow(new RuntimeException("Error interno"));

        // Act
        ResponseEntity<AuthResponse> response = authController.register(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull(); // No debería haber cuerpo en caso de error 500
    }
}
