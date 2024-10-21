package com.nextstep.nextstepBackEnd.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Gasto;
import com.nextstep.nextstepBackEnd.model.Rol;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.CategoriaRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.service.GastoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class GastoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CategoriaRepository categoriaRepository;

    @MockBean
    private GastoService gastoService;

    @InjectMocks
    private GastoController gastoController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Gasto mockGasto;
    private Usuario mockUser;
    private Categoria mockCategoria;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar el mockUser con un rol
        mockUser = new Usuario();
        mockUser.setId(1);
        mockUser.setUsername("usuario_test");
        mockUser.setEmail("test@example.com");
        mockUser.setRol(Rol.normal);

        // Configurar mockCategoria
        mockCategoria = new Categoria();
        mockCategoria.setId(1);
        mockCategoria.setNombre("Alimentación");
        mockCategoria.setDescripcion("Gastos de comida y bebida");

        // Configurar mockGasto
        mockGasto = new Gasto();
        mockGasto.setId(1);
        mockGasto.setNombre("Compra");
        mockGasto.setMonto(BigDecimal.valueOf(50.00));
        mockGasto.setUsuario(mockUser);
        mockGasto.setCategoria(mockCategoria);

        // Configurar ObjectMapper para ignorar los campos que no se pueden serializar
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.addMixIn(Usuario.class, UsuarioMixin.class);
    }

    // Clase Mixin para ignorar campos específicos en Usuario
    @JsonIgnoreProperties({"authorities"})
    private abstract class UsuarioMixin {
    }


    @Test
    @WithMockUser(roles = "normal")
    public void testGetGastosByUsuario() throws Exception {
        // Simula que se encuentra el usuario
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        // Simula que se devuelve una lista de gastos
        when(gastoService.getGastosByUsuarioId(1)).thenReturn(List.of(mockGasto));

        mockMvc.perform(get("/gastos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Compra"));

        verify(gastoService, times(1)).getGastosByUsuarioId(1);
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testCreateGasto() throws Exception {
        // Simula la existencia de un usuario y una categoría
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(mockCategoria));

        // Simula la creación del gasto
        when(gastoService.createGasto(eq(1), eq(1), any(Gasto.class))).thenReturn(mockGasto);

        mockMvc.perform(post("/gastos/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockGasto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Compra"));

        verify(gastoService, times(1)).createGasto(eq(1), eq(1), any(Gasto.class));
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testUpdateGasto() throws Exception {
        when(gastoService.updateGasto(eq(1), any(Gasto.class))).thenReturn(mockGasto);

        mockMvc.perform(put("/gastos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockGasto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Compra"));

        verify(gastoService, times(1)).updateGasto(eq(1), any(Gasto.class));
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testDeleteGasto() throws Exception {
        doNothing().when(gastoService).deleteGasto(1);

        mockMvc.perform(delete("/gastos/1"))
                .andExpect(status().isOk());

        verify(gastoService, times(1)).deleteGasto(1);
    }
}
