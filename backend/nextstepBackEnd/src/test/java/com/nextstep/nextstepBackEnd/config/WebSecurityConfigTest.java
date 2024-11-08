package com.nextstep.nextstepBackEnd.config;

import com.nextstep.nextstepBackEnd.authTest.TestController;
import com.nextstep.nextstepBackEnd.controller.AuthController;
import com.nextstep.nextstepBackEnd.jwt.JwtAuthFilter;
import com.nextstep.nextstepBackEnd.jwt.JwtService;
import com.nextstep.nextstepBackEnd.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthController.class, TestController.class})
@Import(WebSecurityConfig.class)
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private AuthenticationProvider authProvider;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setup() throws Exception {
        // Configurar jwtAuthFilter para que permita el acceso a rutas públicas y bloquee las protegidas
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain filterChain = invocation.getArgument(2);

            // Verificar si la URI es pública
            String requestURI = request.getRequestURI();
            if (requestURI.equals("/auth/login") || requestURI.equals("/auth/register")) {
                // Para rutas públicas, permite el acceso sin modificar la respuesta
                filterChain.doFilter(request, response);
            } else {
                // Para rutas protegidas, simular el bloqueo con una respuesta 401 Unauthorized
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }

            return null;
        }).when(jwtAuthFilter).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterChain.class));
    }


    @Test
    public void shouldAllowAccessToLoginAndRegisterWithoutAuthentication() throws Exception {
        // JSON para login
        String loginJson = "{\"username\":\"testuser\", \"password\":\"password\"}";

        // JSON para register (ajusta los campos según lo que el controlador requiera)
        String registerJson = "{\"username\":\"newuser\", \"password\":\"password\", \"email\":\"newuser@example.com\"}";

        // Prueba de login (espera 200 OK)
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk());

        // Prueba de register (espera 201 Created)
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isCreated());
    }




    @Test
    public void shouldRejectAccessToProtectedEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/protegido/test"))
                .andExpect(status().isUnauthorized()); // Espera un 401 Unauthorized
    }
}
