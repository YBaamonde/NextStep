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


    // Test para obtener el perfil del usuario
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


    // Test para actualizar la contraseña
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



    // Test para eliminar el usuario
    @Test
    void deleteAccount() {
        // Simulación de existencia del usuario
        when(userRepository.existsById(1)).thenReturn(true);

        perfilService.deleteAccount(1);

        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(1)).deleteById(1);
    }


    // Test para obtener el perfil del usuario
    @Test
    void getProfileThrowsExceptionWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> perfilService.getProfile(1));

        verify(userRepository, times(1)).findById(1);
    }


    // Test para actualizar la contraseña
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


    // Test para eliminar el usuario
    @Test
    void deleteAccountThrowsExceptionWhenUserNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> perfilService.deleteAccount(1));

        verify(userRepository, times(1)).existsById(1);
    }


    // Test para actualizar el username de un usuario
    @Test
    void updateUsername() {
        // Configurar el usuario actual en la base de datos simulada
        int userId = 1;
        String newUsername = "nuevoNombreDeUsuario";

        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setUsername("nombreAntiguo");
        usuario.setPassword("password123");

        // Configurar el mock para encontrar el usuario por ID
        when(userRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(userRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutar el metodo de actualización
        perfilService.updateUsername(userId, newUsername);

        // Verificar que el nombre de usuario se haya actualizado
        assertEquals(newUsername, usuario.getUsername());

        // Verificar que el repositorio haya llamado a save con el usuario actualizado
        verify(userRepository, times(1)).save(usuario);
    }


}
