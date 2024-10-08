package com.nextstep.nextstepBackEnd.auth;

import com.nextstep.nextstepBackEnd.jwt.JwtService;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    // Mocks para simular las dependencias de AuthService
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    // InjectMocks para inyectar las dependencias simuladas en AuthService
    @InjectMocks
    private AuthService authService;

    // Inicializa los mocks antes de cada test
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para el método login
    @Test
    public void testLogin() {
        // Arrange: Configura el escenario de prueba
        String username = "testuser";
        String password = "testpassword";
        String token = "testtoken";
        Usuario user = mock(Usuario.class); // Simula un objeto Usuario

        // Simula el comportamiento de los métodos del repositorio y el servicio JWT
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtService.getToken(user)).thenReturn(token);

        // Act: Ejecuta el método a probar
        AuthResponse response = authService.login(new LoginRequest(username, password));

        // Assert: Verifica que el comportamiento sea el esperado
        assertEquals(token, response.getToken()); // Verifica que el token generado es el esperado
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class)); // Verifica que se autentique correctamente
        verify(userRepository).findByUsername(username); // Verifica que el repositorio fue llamado correctamente
        verify(jwtService).getToken(user); // Verifica que se generó un token para el usuario
    }

    // Test para el metodo register
    @Test
    public void testRegister() {
        // Arrange: Configura el escenario de prueba
        String username = "testuser";
        String password = "testpassword";
        String encodedPassword = "encodedpassword";
        String token = "testtoken";
        RegisterRequest request = new RegisterRequest("Test User", username, password, "normal");

        // Simula el comportamiento del codificador de contraseñas y del servicio JWT
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(jwtService.getToken(any(Usuario.class))).thenReturn(token);

        // Act: Ejecuta el metodo a probar
        AuthResponse response = authService.register(request);

        // Assert: Verifica que el comportamiento sea el esperado
        assertEquals(token, response.getToken()); // Verifica que el token generado es el esperado
        verify(userRepository).save(any(Usuario.class)); // Verifica que el usuario fue guardado en el repositorio
        verify(passwordEncoder).encode(password); // Verifica que la contraseña fue codificada correctamente
        verify(jwtService).getToken(any(Usuario.class)); // Verifica que se generó un token para el usuario registrado
    }

    // Test para login con credenciales incorrectas
    @Test
    public void testLoginWithInvalidCredentials() {
        // Arrange: Configura el escenario de prueba con un usuario no existente
        String username = "invalidUser";
        String password = "invalidPassword";

        // Simula que no se encuentra el usuario
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert: Verifica que se lance una excepción al intentar iniciar sesión con credenciales incorrectas
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login(new LoginRequest(username, password));
        });

        assertEquals("Invalid credentials", exception.getMessage()); // Verifica que la excepción tenga el mensaje correcto
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class)); // Verifica que no se intenta autenticar
        verify(jwtService, never()).getToken(any(UserDetails.class)); // Verifica que no se genera un token
    }

    // Test para registro con un nombre de usuario ya existente
    @Test
    public void testRegisterWithExistingUsername() {
        // Configura el escenario de prueba con un nombre de usuario existente
        String username = "existingUser";
        String email = "test@example.com"; // Agrega un correo ficticio
        String password = "testpassword";
        RegisterRequest request = new RegisterRequest(username, email, password, "normal");

        // Simula que ya existe un usuario con ese nombre
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new Usuario()));

        // Verifica que se lance una excepción al intentar registrar un usuario con un nombre de usuario ya existente
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.register(request);
        });

        assertEquals("Username already taken", exception.getMessage());
        verify(userRepository, never()).save(any(Usuario.class));
        verify(passwordEncoder, never()).encode(password);
        verify(jwtService, never()).getToken(any(Usuario.class));
    }


    // Test para registro con un email ya existente
    @Test
    public void testRegisterWithExistingEmail() {
        // Configura el escenario de prueba con un correo existente
        String username = "newUser";
        String email = "existingUser@example.com"; // Mantén el mismo orden
        String password = "testpassword";
        RegisterRequest request = new RegisterRequest(username, email, password, "normal");

        // Simula que ya existe un usuario con ese correo
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new Usuario()));

        // Verifica que se lance una excepción al intentar registrar un usuario con un correo ya existente
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.register(request);
        });

        assertEquals("Email already exists", exception.getMessage()); // Verifica que la excepción tenga el mensaje correcto
        verify(userRepository, never()).save(any(Usuario.class)); // Verifica que no se intenta guardar un usuario nuevo
        verify(passwordEncoder, never()).encode(password); // Verifica que no se codifica la contraseña
        verify(jwtService, never()).getToken(any(Usuario.class)); // Verifica que no se genera un token
    }


}
