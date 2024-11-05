package com.nextstep.nextstepBackEnd.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.nextstep.nextstepBackEnd.model.*;
import com.nextstep.nextstepBackEnd.repository.CategoriaRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.service.GastoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    private ObjectMapper objectMapper;
    private Gasto mockGasto;
    private GastoDTO mockGastoDTO;
    private Usuario mockUser;
    private Categoria mockCategoria;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuración de ObjectMapper
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Manejar LocalDate
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        mockUser = new Usuario();
        mockUser.setId(1);
        mockUser.setUsername("usuario_test");
        mockUser.setEmail("test@example.com");
        mockUser.setRol(Rol.normal);

        mockCategoria = new Categoria();
        mockCategoria.setId(1);
        mockCategoria.setNombre("Alimentación");
        mockCategoria.setDescripcion("Gastos de comida y bebida");

        mockGasto = new Gasto();
        mockGasto.setId(1);
        mockGasto.setNombre("Compra");
        mockGasto.setMonto(BigDecimal.valueOf(50.00));
        mockGasto.setFecha(LocalDate.now());
        mockGasto.setUsuario(mockUser);
        mockGasto.setCategoria(mockCategoria);

        mockGastoDTO = new GastoDTO(
                mockGasto.getId(),
                mockGasto.getNombre(),
                mockGasto.getMonto(),
                mockGasto.getFecha(),
                mockGasto.getCategoria().getId()
        );
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testGetGastosByUsuario() throws Exception {
        when(gastoService.getGastosByUsuarioId(1)).thenReturn(List.of(mockGastoDTO)); // Use mockGastoDTO

        mockMvc.perform(get("/gastos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Compra"))
                .andExpect(jsonPath("$[0].monto").value(50.00));

        verify(gastoService, times(1)).getGastosByUsuarioId(1);
    }


    @Test
    @WithMockUser(roles = "normal")
    public void testCreateGasto() throws Exception {
        when(gastoService.createGasto(eq(1), eq(1), any(GastoDTO.class))).thenReturn(mockGastoDTO);

        mockMvc.perform(post("/gastos/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockGastoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Compra"));

        verify(gastoService, times(1)).createGasto(eq(1), eq(1), any(GastoDTO.class));
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testUpdateGasto() throws Exception {
        when(gastoService.updateGasto(eq(1), any(GastoDTO.class))).thenReturn(mockGastoDTO);

        mockMvc.perform(put("/gastos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockGastoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Compra"));

        verify(gastoService, times(1)).updateGasto(eq(1), any(GastoDTO.class));
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
