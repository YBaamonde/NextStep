package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextstep.nextstepBackEnd.auth.AdminRegisterRequest;
import com.nextstep.nextstepBackEnd.auth.AuthResponse;
import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    // Test que simula Admin creando un usuario
    @Test
    @WithMockUser(authorities = "admin")  // Simula que un usuario con autoridad 'admin' hace la petición
    public void shouldCreateUserSuccessfully() throws Exception {
        AdminRegisterRequest registerRequest = new AdminRegisterRequest();
        registerRequest.setUsername("nuevoAdmin");
        registerRequest.setEmail("nuevoAdmin@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRol("admin");

        AuthResponse authResponse = AuthResponse.builder()
                .token("dummyToken")
                .build();

        // Simular el comportamiento de AuthService
        Mockito.when(authService.registerAdmin(Mockito.any(AdminRegisterRequest.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/admin/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isOk())  // Verifica que la respuesta es 200 OK
                .andExpect(content().string("Usuario creado exitosamente"));

        Mockito.verify(authService, Mockito.times(1)).registerAdmin(Mockito.any(AdminRegisterRequest.class));
    }


    // Test para listar todos los usuarios
    @Test
    @WithMockUser(authorities = "admin")  // Simula que un usuario con autoridad 'admin' hace la petición
    public void shouldReturnListOfUsers() throws Exception {
        List<Usuario> users = Arrays.asList(
                new Usuario(1, "adminUser", "admin@example.com", "password123", Rol.admin),
                new Usuario(2, "normalUser", "normal@example.com", "password123", Rol.normal)
        );

        // Simula el comportamiento de UserRepository
        Mockito.when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Verifica que la respuesta es 200 OK
                .andExpect(jsonPath("$.size()").value(2))  // Verifica que el tamaño de la lista es 2
                .andExpect(jsonPath("$[0].username").value("adminUser"))  // Verifica el nombre del primer usuario
                .andExpect(jsonPath("$[1].username").value("normalUser"));  // Verifica el nombre del segundo usuario

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }


    // Test para eliminar un usuario con éxito
    @Test
    @WithMockUser(authorities = "admin")  // Simula que un usuario con autoridad 'admin' hace la petición
    public void shouldDeleteUserSuccessfully() throws Exception {
        // Simula el comportamiento de UserRepository al eliminar un usuario
        Mockito.doNothing().when(userRepository).deleteById(1);

        mockMvc.perform(delete("/admin/delete-user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Verifica que la respuesta es 200 OK
                .andExpect(content().string("Usuario eliminado exitosamente"));

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1);
    }


    // Test para verificar acceso denegado a usuarios sin rol admin
    @Test
    @WithMockUser(authorities = "normal")  // Simula que un usuario con autoridad 'normal' hace la petición
    public void shouldDenyAccessToNonAdminUser() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden());  // Verifica que el acceso es denegado
    }

}

