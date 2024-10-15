package com.nextstep.nextstepBackEnd.config;

import com.nextstep.nextstepBackEnd.config.AdminConfig;
import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.config.AdminInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminConfig adminConfig;

    @InjectMocks
    private AdminInitializer adminInitializer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    // Test para verificar la creación de un usuario admin cuando no existe
    @Test
    public void shouldCreateAdminUserWhenNotExists() {
        // Arrange: Configurar los valores que se usarán
        when(adminConfig.getAdminEmail()).thenReturn("admin@example.com");
        when(adminConfig.getAdminUsername()).thenReturn("admin");
        when(adminConfig.getAdminPassword()).thenReturn("adminpass");
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());  // Simula que no existe un usuario admin

        // Simula la encriptación de la contraseña
        when(passwordEncoder.encode("adminpass")).thenReturn("encodedAdminPass");

        // Act: Ejecuta el metodo de inicialización del admin
        adminInitializer.initAdminUser();

        // Assert: Verifica que se haya llamado a los métodos correctos
        verify(userRepository, times(1)).findByEmail("admin@example.com");
        verify(passwordEncoder, times(1)).encode("adminpass");
        verify(userRepository, times(1)).save(any(Usuario.class));  // Verifica que se guardó el usuario admin
    }

    // Test para verificar que no se crea un usuario admin si ya existe
    @Test
    public void shouldNotCreateAdminUserIfExists() {
        // Arrange: Simula que ya existe un usuario admin
        when(adminConfig.getAdminEmail()).thenReturn("admin@example.com");
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(new Usuario()));

        // Act: Ejecuta el metodo de inicialización del admin
        adminInitializer.initAdminUser();

        // Assert: Verifica que no se intente guardar un nuevo usuario admin
        verify(userRepository, times(1)).findByEmail("admin@example.com");
        verify(userRepository, never()).save(any(Usuario.class));  // Verifica que no se guardó ningún usuario nuevo
    }
}
