package com.nextstep.nextstepBackEnd.config;

import com.nextstep.nextstepBackEnd.auth.AuthController; // Importa tu controlador existente
import com.nextstep.nextstepBackEnd.auth.AuthService; // Importa tu servicio
import com.nextstep.nextstepBackEnd.authTest.TestController;
import com.nextstep.nextstepBackEnd.jwt.JwtAuthFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; // Cambia esto
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthController.class, TestController.class}) // Cargamos AuthController y TestController
@Import(WebSecurityConfig.class) // Importamos la configuración de seguridad
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private AuthenticationProvider authProvider;

    @MockBean
    private AuthService authService;

    @BeforeEach
    public void setup() {
        // No es necesario crear MockMvc de nuevo, se inyecta directamente
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

