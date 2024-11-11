package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.PagoDTO;
import com.nextstep.nextstepBackEnd.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    @InjectMocks
    private PagoController pagoController;

    private ObjectMapper objectMapper;
    private PagoDTO mockPagoDTO;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        mockPagoDTO = new PagoDTO(
                1,
                "Internet",
                BigDecimal.valueOf(50.00),
                LocalDate.now(),
                true,
                Pago.Frecuencia.MENSUAL
        );
    }

    @Test
    @WithMockUser(roles = "normal")  // Simula un usuario autenticado con rol normal
    public void testGetPagosByUsuario() throws Exception {
        when(pagoService.getPagosByUsuarioId(1)).thenReturn(List.of(mockPagoDTO));

        mockMvc.perform(get("/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Internet"))
                .andExpect(jsonPath("$[0].monto").value(50.00));

        verify(pagoService, times(1)).getPagosByUsuarioId(1);
    }

    @Test
    @WithMockUser(roles = "normal")  // Simula un usuario autenticado con rol normal
    public void testCreatePago() throws Exception {
        when(pagoService.createPago(eq(1), any(PagoDTO.class))).thenReturn(mockPagoDTO);

        mockMvc.perform(post("/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPagoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Internet"));

        verify(pagoService, times(1)).createPago(eq(1), any(PagoDTO.class));
    }

    @Test
    @WithMockUser(roles = "normal")  // Simula un usuario autenticado con rol normal
    public void testUpdatePago() throws Exception {
        when(pagoService.updatePago(eq(1), any(PagoDTO.class))).thenReturn(mockPagoDTO);

        mockMvc.perform(put("/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPagoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Internet"));

        verify(pagoService, times(1)).updatePago(eq(1), any(PagoDTO.class));
    }

    @Test
    @WithMockUser(roles = "normal")  // Simula un usuario autenticado con rol normal
    public void testDeletePago() throws Exception {
        doNothing().when(pagoService).deletePago(1);

        mockMvc.perform(delete("/pagos/1"))
                .andExpect(status().isOk());

        verify(pagoService, times(1)).deletePago(1);
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testGetPagosRecurrentesByUsuario() throws Exception {
        when(pagoService.getPagosRecurrentesByUsuarioId(1)).thenReturn(List.of(mockPagoDTO));

        mockMvc.perform(get("/pagos/recurrentes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Internet"))
                .andExpect(jsonPath("$[0].recurrente").value(true))
                .andExpect(jsonPath("$[0].frecuencia").value("MENSUAL"));

        verify(pagoService, times(1)).getPagosRecurrentesByUsuarioId(1);
    }
}
