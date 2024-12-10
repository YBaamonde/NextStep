package com.nextstep.nextstepBackEnd.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextstep.nextstepBackEnd.model.*;
import com.nextstep.nextstepBackEnd.service.GastoService;
import com.nextstep.nextstepBackEnd.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class InicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GastoService gastoService;

    @MockBean
    private PagoService pagoService;

    private Map<String, Object> inicioData;

    @BeforeEach
    public void setUp() {
        inicioData = new HashMap<>();
        inicioData.put("pagosProximos", List.of(
                new PagoDTO(
                        1,
                        "Internet",
                        BigDecimal.valueOf(50.00),
                        LocalDate.now().plusDays(5),
                        true,
                        Pago.Frecuencia.MENSUAL
                )
        ));
        inicioData.put("gastosPorCategoria", Map.of("Casa", 1000.0));
        inicioData.put("evolucionTrimestral", Map.of("Q1", 2000.0));
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testGetInicioData() throws Exception {
        when(pagoService.getPagosConRecurrencia(eq(1))).thenReturn((List<PagoDTO>) inicioData.get("pagosProximos"));
        when(gastoService.getGastosPorCategoria(eq(1))).thenReturn(new HashMap<>(Map.of("Casa", BigDecimal.valueOf(1000))));
        when(gastoService.getGastosPorTrimestre(eq(1))).thenReturn(new HashMap<>(Map.of("Q1", 2000.0)));

        mockMvc.perform(get("/inicio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagosProximos[0].nombre").value("Internet"))
                .andExpect(jsonPath("$.gastosPorCategoria.Casa").value(1000.0))
                .andExpect(jsonPath("$.evolucionTrimestral.Q1").value(2000.0));

        verify(pagoService, times(1)).getPagosConRecurrencia(eq(1));
        verify(gastoService, times(1)).getGastosPorCategoria(eq(1));
        verify(gastoService, times(1)).getGastosPorTrimestre(eq(1));
    }


    @Test
    @WithMockUser(roles = "normal")
    void testGetInicioDataThrowsExceptionWhenUserNotFound() throws Exception {
        // Mockear excepción para un usuario no encontrado
        when(pagoService.getPagosConRecurrencia(99)).thenThrow(new IllegalArgumentException("Usuario no encontrado."));

        // Ejecutar y verificar el manejo de la excepción
        mockMvc.perform(get("/inicio/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Usuario no encontrado."));

        // Verificar interacciones
        verify(pagoService, times(1)).getPagosConRecurrencia(99);
        verifyNoInteractions(gastoService); // gastoService no debería ser llamado si el usuario no existe
    }

    @Test
    @WithMockUser(roles = "normal")
    void testGetInicioDataHandlesUnexpectedException() throws Exception {
        // Mockear una excepción genérica
        when(pagoService.getPagosConRecurrencia(1)).thenThrow(new RuntimeException("Error inesperado"));

        // Ejecutar y verificar el manejo de la excepción genérica
        mockMvc.perform(get("/inicio/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error inesperado: Error inesperado"));

        // Verificar interacciones
        verify(pagoService, times(1)).getPagosConRecurrencia(1);
        verifyNoInteractions(gastoService); // gastoService no debería ser llamado si ocurre un error inesperado
    }
}
