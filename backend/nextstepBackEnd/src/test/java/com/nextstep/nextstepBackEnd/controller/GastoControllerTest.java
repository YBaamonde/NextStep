package com.nextstep.nextstepBackEnd.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.nextstep.nextstepBackEnd.model.*;
import com.nextstep.nextstepBackEnd.service.GastoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private GastoService gastoService;

    private ObjectMapper objectMapper;
    private GastoDTO mockGastoDTO;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        mockGastoDTO = new GastoDTO(
                1,
                "Compra",
                BigDecimal.valueOf(50.00),
                LocalDate.now(),
                1
        );
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testGetGastosByUsuario() throws Exception {
        when(gastoService.getGastosByUsuarioId(1)).thenReturn(List.of(mockGastoDTO));

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

    @Test
    @WithMockUser(roles = "normal")
    public void testGetGastosHistoricosPorCategoria() throws Exception {
        when(gastoService.getGastosByCategoriaConLimite(eq(1), eq(Integer.MAX_VALUE)))
                .thenReturn(List.of(mockGastoDTO));

        mockMvc.perform(get("/gastos/categoria/1/historicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Compra"))
                .andExpect(jsonPath("$[0].categoriaId").value(1));

        verify(gastoService, times(1)).getGastosByCategoriaConLimite(eq(1), eq(Integer.MAX_VALUE));
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testGetGastosPorCategoriaConLimite() throws Exception {
        when(gastoService.getGastosByCategoriaConLimite(eq(1), eq(5))).thenReturn(List.of(mockGastoDTO));

        mockMvc.perform(get("/gastos/categoria/1").param("limite", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Compra"))
                .andExpect(jsonPath("$[0].categoriaId").value(1));

        verify(gastoService, times(1)).getGastosByCategoriaConLimite(eq(1), eq(5));
    }
}
