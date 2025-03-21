package com.nextstep.nextstepBackEnd.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.nextstep.nextstepBackEnd.auth.AdminRegisterRequest;
import com.nextstep.nextstepBackEnd.auth.AuthResponse;
import com.nextstep.nextstepBackEnd.auth.LoginRequest;
import com.nextstep.nextstepBackEnd.auth.RegisterRequest;
import com.nextstep.nextstepBackEnd.exception.InvalidCredentialsException;
import com.nextstep.nextstepBackEnd.jwt.JwtService;
import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        // Arrange
        String token = "testtoken";
        LoginRequest request = new LoginRequest("testuser", "testemail@example.com", "testpassword");
        Usuario user = Usuario.builder()
                .username("testuser")
                .email("testemail@example.com")
                .password("testpassword")
                .build();

        // Simulamos que el usuario existe
        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
        // Simulamos que el JWT se genera correctamente
        when(jwtService.generateToken(any(Usuario.class))).thenReturn(token);

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(token);

        // Verifica que la autenticación se haya llamado con los valores correctos
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testuser", "testpassword")
        );
    }

    @Test
    public void testLoginInvalidCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest("invaliduser", "invalidemail@example.com", "invalidpassword");

        // Simulamos que el usuario no existe
        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("Invalid credentials");

        // Verifica que el metodo de autenticación no se llame si no se encuentra el usuario
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testRegisterSuccess() {
        // Arrange
        String token = "testtoken";
        RegisterRequest request = new RegisterRequest("newuser", "newemail@example.com", "newpassword");

        // Simulamos que no existe un usuario con el mismo nombre ni email
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        // Simulamos que el password encoder funciona correctamente
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        // Simulamos que el JWT se genera correctamente
        when(jwtService.generateToken(any(Usuario.class))).thenReturn(token);

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(token);

        // Verifica que el usuario fue guardado en el repositorio
        verify(userRepository).save(any(Usuario.class));
    }

    @Test
    public void testRegisterUsernameTaken() {
        // Arrange
        RegisterRequest request = new RegisterRequest("existinguser", "newemail@example.com", "newpassword");

        // Simulamos que el nombre de usuario ya existe
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new Usuario()));

        // Act & Assert
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("El nombre de usuario ya existe");

        // Verifica que no se haya llamado al repositorio para guardar un nuevo usuario
        verify(userRepository, never()).save(any(Usuario.class));
    }

    @Test
    public void testRegisterEmailTaken() {
        // Arrange
        RegisterRequest request = new RegisterRequest("newuser", "existingemail@example.com", "newpassword");

        // Simulamos que el email ya existe
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new Usuario()));

        // Act & Assert
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("El email ya existe");

        // Verifica que no se haya llamado al repositorio para guardar un nuevo usuario
        verify(userRepository, never()).save(any(Usuario.class));
    }

    // Test para registrar un usuario admin
    @Test
    public void testRegisterAdminSuccess() {
        AdminRegisterRequest request = new AdminRegisterRequest();
        request.setUsername("adminUser");
        request.setEmail("admin@example.com");
        request.setPassword("password123");
        request.setRol("admin");

        // Simulamos que no existe un usuario con el mismo nombre ni email
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        // Simulamos que el password encoder funciona correctamente
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        // Simulamos que el JWT se genera correctamente
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("testtoken");

        // Act
        AuthResponse response = authService.registerAdmin(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("testtoken");

        // Verifica que el usuario fue guardado en el repositorio con el rol admin
        verify(userRepository).save(any(Usuario.class));
    }
}
