package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.PagoDTO;
import com.nextstep.nextstepBackEnd.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    @WithMockUser(roles = "normal") // Simula un usuario autenticado con rol normal
    public void testGetPagosByUsuario() throws Exception {
        when(pagoService.getPagosByUsuarioId(1)).thenReturn(List.of(mockPagoDTO));

        mockMvc.perform(get("/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Internet"))
                .andExpect(jsonPath("$[0].monto").value(50.00));

        verify(pagoService, times(1)).getPagosByUsuarioId(1);
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testGetPagosByUsuario_UserNotFound() throws Exception {
        when(pagoService.getPagosByUsuarioId(1)).thenThrow(new IllegalArgumentException("Usuario no encontrado."));

        mockMvc.perform(get("/pagos/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Usuario no encontrado."));

        verify(pagoService, times(1)).getPagosByUsuarioId(1);
    }


    @Test
    @WithMockUser(roles = "normal") // Simula un usuario autenticado con rol normal
    public void testCreatePago() throws Exception {
        when(pagoService.createPago(eq(1), any(PagoDTO.class))).thenReturn(mockPagoDTO);

        mockMvc.perform(post("/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPagoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Internet"));

        verify(pagoService, times(1)).createPago(eq(1), any(PagoDTO.class));
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testCreatePago_InvalidData() throws Exception {
        mockPagoDTO.setNombre(null); // Nombre inválido

        when(pagoService.createPago(eq(1), any(PagoDTO.class)))
                .thenThrow(new IllegalArgumentException("El nombre del pago es obligatorio."));

        mockMvc.perform(post("/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPagoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El nombre del pago es obligatorio."));

        verify(pagoService, times(1)).createPago(eq(1), any(PagoDTO.class));
    }


    @Test
    @WithMockUser(roles = "normal") // Simula un usuario autenticado con rol normal
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
    @WithMockUser(roles = "normal")
    public void testUpdatePago_PagoNotFound() throws Exception {
        when(pagoService.updatePago(eq(1), any(PagoDTO.class)))
                .thenThrow(new IllegalArgumentException("Pago no encontrado."));

        mockMvc.perform(put("/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPagoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Pago no encontrado."));

        verify(pagoService, times(1)).updatePago(eq(1), any(PagoDTO.class));
    }


    @Test
    @WithMockUser(roles = "normal") // Simula un usuario autenticado con rol normal
    public void testDeletePago() throws Exception {
        doNothing().when(pagoService).deletePago(1);

        mockMvc.perform(delete("/pagos/1"))
                .andExpect(status().isNoContent());

        verify(pagoService, times(1)).deletePago(1);
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testDeletePago_PagoNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Pago no encontrado.")).when(pagoService).deletePago(99);

        mockMvc.perform(delete("/pagos/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Pago no encontrado."));

        verify(pagoService, times(1)).deletePago(99);
    }


    @Test
    @WithMockUser(roles = "normal")
    public void testGetPagosRecurrentesByUsuario() throws Exception {
        when(pagoService.getPagosConRecurrencia(1)).thenReturn(List.of(mockPagoDTO));

        mockMvc.perform(get("/pagos/recurrentes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Internet"))
                .andExpect(jsonPath("$[0].recurrente").value(true))
                .andExpect(jsonPath("$[0].frecuencia").value("MENSUAL"));

        verify(pagoService, times(1)).getPagosConRecurrencia(1);
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testGetPagosRecurrentesByUsuario_UserNotFound() throws Exception {
        when(pagoService.getPagosConRecurrencia(1)).thenThrow(new IllegalArgumentException("Usuario no encontrado."));

        mockMvc.perform(get("/pagos/recurrentes/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Usuario no encontrado."));

        verify(pagoService, times(1)).getPagosConRecurrencia(1);
    }

}
