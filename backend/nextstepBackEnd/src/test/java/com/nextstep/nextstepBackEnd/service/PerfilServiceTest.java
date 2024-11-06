package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.jwt.JwtService;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private PerfilService perfilService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("testuser");
        usuario.setEmail("testuser@example.com");
        usuario.setPassword("password"); // Simulación para las pruebas
    }

    @Test
    void getProfile() {
        // Simulación del repositorio para que devuelva el usuario mockeado
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));

        Usuario result = perfilService.getProfile(1);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("testuser@example.com", result.getEmail());

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void updatePassword() {
        // Configuración del PasswordEncoder para simular la codificación de la nueva contraseña
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedPassword");

        // Configuración del repositorio para que devuelva el usuario mockeado
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));

        // Configuración de JwtService para devolver un token simulado cuando se le pasa el objeto usuario
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");

        // Mock del metodo save en el repositorio de usuarios
        when(userRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutar el metodo de actualización de contraseña
        boolean result = perfilService.updatePassword(1, "newPassword123");

        // Verificaciones
        assertTrue(result);
        verify(passwordEncoder, times(1)).encode("newPassword123");
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(usuario);
    }





    @Test
    void deleteAccount() {
        // Simulación de existencia del usuario
        when(userRepository.existsById(1)).thenReturn(true);

        perfilService.deleteAccount(1);

        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void getProfileThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> perfilService.getProfile(1));

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void updatePasswordThrowsExceptionWhenUserNotFound() {
        // Configuración para que userRepository devuelva vacío (simulando usuario no encontrado)
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Ejecutar el metodo y verificar que lanza ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> perfilService.updatePassword(1, "newPassword123"));

        // Verificar que el metodo findById fue llamado una vez
        verify(userRepository, times(1)).findById(1);
        verifyNoMoreInteractions(userRepository, jwtService, passwordEncoder);
    }


    @Test
    void deleteAccountThrowsExceptionWhenUserNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> perfilService.deleteAccount(1));

        verify(userRepository, times(1)).existsById(1);
    }

}
