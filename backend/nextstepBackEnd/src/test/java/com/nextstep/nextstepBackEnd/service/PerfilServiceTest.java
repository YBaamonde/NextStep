package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTest {

    @Mock
    private UserRepository userRepository;

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
        // Simulación del repositorio para que devuelva el usuario al buscar por ID
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(userRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Actualizar la contraseña
        boolean result = Boolean.parseBoolean(perfilService.updatePassword(1, "newPassword123"));

        assertTrue(result);
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
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> perfilService.updatePassword(1, "newPassword123"));

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void deleteAccountThrowsExceptionWhenUserNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> perfilService.deleteAccount(1));

        verify(userRepository, times(1)).existsById(1);
    }
}
