package com.nextstep.nextstepBackEnd.config;

import com.nextstep.nextstepBackEnd.auth.AuthController; // Importa tu controlador existente
import com.nextstep.nextstepBackEnd.auth.AuthService; // Importa tu servicio
import com.nextstep.nextstepBackEnd.jwt.JwtAuthFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; // Cambia esto
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class) // Solo cargamos el AuthController
@Import(WebSecurityConfig.class) // Importamos la configuración de seguridad
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Cambiado de @Mock a @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean // Cambiado de @Mock a @MockBean
    private AuthenticationProvider authProvider;

    @MockBean // Cambiado de @Mock a @MockBean
    private AuthService authService; // Mock de AuthService

    @InjectMocks
    private WebSecurityConfig webSecurityConfig;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // MockMvc ya está configurado a través de @WebMvcTest
    }

    @Test
    public void shouldAllowAccessToLoginAndRegisterWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRejectAccessToProtectedEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/protegido/test"))
                .andExpect(status().isUnauthorized()); // Espera un 401
    }




}
