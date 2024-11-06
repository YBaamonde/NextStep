package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class PerfilServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PerfilService perfilService;

    private Usuario usuario;

    @Before
    public void setUp() {
        // Configuración de usuario de prueba
        usuario = Usuario.builder()
                .id(1)
                .username("testuser")
                .email("testuser@example.com")
                .password("hashedPassword") // Simular una contraseña codificada
                .rol(Rol.normal)
                .build();
    }

    // Test para obtener el perfil
    @Test
    public void shouldReturnUserProfile() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(usuario));

        Usuario result = perfilService.getProfile(1);
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    // Test para cambiar la contraseña
    @Test
    public void shouldUpdateUserPassword() {
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(usuario));

        // Simulamos la codificación de contraseña
        String newPassword = "newEncodedPassword";
        perfilService.updatePassword(1, newPassword);

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        Mockito.verify(userRepository).save(usuarioCaptor.capture());
        assertEquals(newPassword, usuarioCaptor.getValue().getPassword());
    }

    // Test para eliminar un usuario existente
    @Test
    public void shouldDeleteUser() {
        Mockito.when(userRepository.existsById(1)).thenReturn(true);

        perfilService.deleteAccount(1);

        Mockito.verify(userRepository).deleteById(1);
    }

    // Test para intentar eliminar un usuario no existente y recibir excepción
    @Test
    public void shouldThrowExceptionWhenDeletingNonExistentUser() {
        Mockito.when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            perfilService.deleteAccount(1);
        });
    }
}

