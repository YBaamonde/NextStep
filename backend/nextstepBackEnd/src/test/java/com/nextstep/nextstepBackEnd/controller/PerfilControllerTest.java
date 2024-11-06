package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextstep.nextstepBackEnd.service.PerfilService;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PerfilService perfilService;

    @InjectMocks
    private PerfilController perfilController;

    private ObjectMapper objectMapper;
    private Usuario mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();

        // Configurar un usuario de ejemplo
        mockUser = new Usuario();
        mockUser.setId(1);
        mockUser.setUsername("testuser");
        mockUser.setEmail("testuser@example.com");
        mockUser.setPassword("password");
    }

    @Test
    @WithMockUser(username = "testuser", roles = "normal")
    public void shouldUpdateUserPassword() throws Exception {
        // Mocking el servicio para que devuelva true, indicando éxito en la actualización de la contraseña
        when(perfilService.updatePassword(anyInt(), any(String.class))).thenReturn(String.valueOf(true));

        // JSON con la nueva contraseña
        String newPasswordJson = "{\"newPassword\":\"newPassword123\"}";

        mockMvc.perform(post("/perfil/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPasswordJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "normal")
    public void shouldGetUserProfile() throws Exception {
        // Mocking para devolver el usuario de ejemplo cuando se llama a perfilService.getProfile()
        when(perfilService.getProfile(anyInt())).thenReturn(mockUser);

        mockMvc.perform(get("/perfil/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "normal")
    public void shouldDeleteUserAccount() throws Exception {
        // Mocking el servicio para que no haga nada en la eliminación del usuario
        doNothing().when(perfilService).deleteAccount(anyInt());

        mockMvc.perform(delete("/perfil/1"))
                .andExpect(status().isOk());
    }
}
