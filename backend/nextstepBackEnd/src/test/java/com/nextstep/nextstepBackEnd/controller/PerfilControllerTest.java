package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.service.PerfilService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PerfilService perfilService;

    private Usuario usuario;

    @Before
    public void setUp() {
        usuario = Usuario.builder()
                .id(1)
                .username("testuser")
                .email("testuser@example.com")
                .build();
    }

    // Test para obtener el perfil del usuario
    @Test
    public void shouldGetUserProfile() throws Exception {
        Mockito.when(perfilService.getProfile(1)).thenReturn(usuario);

        mockMvc.perform(MockMvcRequestBuilders.get("/perfil/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testuser"));
    }

    // Test para actualizar la contraseña del usuario
    @Test
    public void shouldUpdateUserPassword() throws Exception {
        String newPassword = "newPassword";

        mockMvc.perform(MockMvcRequestBuilders.post("/perfil/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newPassword\": \"" + newPassword + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Password updated successfully."));

        Mockito.verify(perfilService).updatePassword(1, newPassword);
    }

    // Test para eliminar el usuario
    @Test
    public void shouldDeleteUserAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/perfil/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Account deleted successfully."));

        Mockito.verify(perfilService).deleteAccount(1);
    }
}
