package com.nextstep.nextstepBackEnd.config;

import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AppConfigTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticationManagerBean() throws Exception {
        // Simular comportamiento de AuthenticationManager
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        // Probar el Bean de AuthenticationManager
        AuthenticationManager result = appConfig.authenticationManager(authenticationConfiguration);
        assertNotNull(result);
        assertEquals(authenticationManager, result);
    }

    @Test
    public void testAuthenticationProviderBean() {
        // Probar el Bean de AuthenticationProvider
        AuthenticationProvider provider = appConfig.authenticationProvider();
        assertNotNull(provider);
        assertInstanceOf(DaoAuthenticationProvider.class, provider);
    }

    @Test
    public void testPasswordEncoderBean() {
        // Probar el Bean de PasswordEncoder
        BCryptPasswordEncoder passwordEncoder = appConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);

        // Verificar que puede encriptar correctamente
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

    @Test
    public void testUserDetailsServiceBean_UserFound() {
        // Crear un usuario simulado
        Usuario mockUser = new Usuario();
        mockUser.setId(1);
        mockUser.setUsername("user");
        mockUser.setPassword("password");
        mockUser.setRol(Rol.normal);

        // Simular que el repositorio devuelve este usuario
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        // Probar el Bean de UserDetailsService
        UserDetailsService userDetailsService = appConfig.userDetailsService();
        assertNotNull(userDetailsService);
        assertDoesNotThrow(() -> userDetailsService.loadUserByUsername("user"));
    }

    @Test
    public void testUserDetailsServiceBean_UserNotFound() {
        // Simular que el usuario no se encuentra
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Probar el Bean de UserDetailsService y que lanza la excepción
        UserDetailsService userDetailsService = appConfig.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistentuser"));
    }
}
