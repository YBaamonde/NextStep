package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextstep.nextstepBackEnd.jwt.JwtService;
import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.service.PerfilService;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PerfilService perfilService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    private ObjectMapper objectMapper; // Objeto para serializar y deserializar JSON
    private Usuario mockUser; // Usuario de ejemplo

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
        mockUser.setRol(Rol.normal);
    }

    // Prueba para actualizar la contraseña del usuario
    @Test
    @WithMockUser(username = "testuser", roles = "normal")
    public void shouldUpdateUserPassword() throws Exception {
        // Configurar el servicio para que devuelva true, indicando éxito en la actualización de la contraseña
        when(perfilService.updatePassword(anyInt(), any(String.class))).thenReturn(true);

        // Mockear el UserRepository para devolver un usuario simulado
        Usuario mockUsuario = new Usuario();
        mockUsuario.setId(1);
        mockUsuario.setUsername("testuser");
        mockUsuario.setPassword("encodedPassword"); // Contraseña codificada simulada
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUsuario));

        // Configurar JwtService para devolver el token simulado
        doReturn("mockedToken").when(jwtService).generateToken(any(UserDetails.class));

        // JSON con la nueva contraseña
        String newPasswordJson = "{\"newPassword\":\"newPassword123\"}";

        // Realizar la solicitud y verificar el estado y contenido de la respuesta
        mockMvc.perform(post("/perfil/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPasswordJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password updated successfully."))
                .andExpect(jsonPath("$.token").value("mockedToken")); // Verificar que el token esté en la respuesta

        // Verificar que se llamaron los métodos correctos en los mocks
        verify(perfilService, times(1)).updatePassword(anyInt(), any(String.class));
        verify(userRepository, times(1)).findById(1);
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));
    }


    // Test para obtener el perfil del usuario
    @Test
    @WithMockUser(username = "testuser", roles = "normal")
    public void shouldGetUserProfile() throws Exception {
        // Mocking para devolver el usuario de ejemplo cuando se llama a perfilService.getProfile()
        when(perfilService.getProfile(anyInt())).thenReturn(mockUser);

        mockMvc.perform(get("/perfil/1"))
                .andExpect(status().isOk());
    }


    // Test para eliminar el usuario
    @Test
    @WithMockUser(username = "testuser", roles = "normal")
    public void shouldDeleteUserAccount() throws Exception {
        // Mocking el servicio para que no haga nada en la eliminación del usuario
        doNothing().when(perfilService).deleteAccount(anyInt());

        mockMvc.perform(delete("/perfil/1"))
                .andExpect(status().isOk());
    }

    // Test para actualizar el nombre de usuario
    @Test
    @WithMockUser(username = "testuser", roles = "normal")
    public void shouldUpdateUsername() throws Exception {
        // Datos de prueba
        int userId = 1;
        String newUsername = "nuevoNombreDeUsuario";
        String token = "mockedToken";

        // Crear el usuario simulado
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setUsername("nombreAntiguo");
        usuario.setPassword("password123");

        // Configurar el servicio de perfil y el servicio JWT para la prueba
        when(userRepository.findById(userId)).thenReturn(Optional.of(usuario));
        // No necesitamos `doNothing()` aquí, simplemente llamamos al metodo sin configurarlo como mock
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn(token);

        // Crear el cuerpo de la solicitud en formato JSON
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("newUsername", newUsername);
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        // Ejecutar la solicitud y verificar el resultado
        mockMvc.perform(put("/perfil/" + userId + "/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Nombre de usuario actualizado correctamente"))
                .andExpect(jsonPath("$.token").value(token));

        // Verificar interacciones
        verify(perfilService, times(1)).updateUsername(userId, newUsername);
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));
    }


}
