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
        inicioData.put("pagosProximos", List.of(new PagoDTO(
                1,
                "Internet",
                BigDecimal.valueOf(50.00),
                LocalDate.now().plusDays(5),
                true,
                Pago.Frecuencia.MENSUAL
        )));
        inicioData.put("gastosPorCategoria", Map.of("Casa", BigDecimal.valueOf(1000)));
        inicioData.put("evolucionTrimestral", Map.of(1, BigDecimal.valueOf(2000)));
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testGetInicioData() throws Exception {
        when(pagoService.getPagosProximosByUsuarioId(1)).thenReturn((List<PagoDTO>) inicioData.get("pagosProximos"));
        when(gastoService.getGastosPorCategoria(1)).thenReturn((Map<String, BigDecimal>) inicioData.get("gastosPorCategoria"));
        when(gastoService.getGastosPorTrimestre(1)).thenReturn((Map<Integer, BigDecimal>) inicioData.get("evolucionTrimestral"));

        mockMvc.perform(get("/inicio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagosProximos[0].nombre").value("Internet"))
                .andExpect(jsonPath("$.gastosPorCategoria.Casa").value(1000))
                .andExpect(jsonPath("$.evolucionTrimestral['1']").value(2000));


        verify(pagoService, times(1)).getPagosProximosByUsuarioId(1);
        verify(gastoService, times(1)).getGastosPorCategoria(1);
        verify(gastoService, times(1)).getGastosPorTrimestre(1);
    }

    /*

    @Test
    @WithMockUser(roles = "normal")
    void testGetInicioDataThrowsExceptionWhenUserNotFound() throws Exception {
        when(gastoService.getGastosPorCategoria(99)).thenThrow(new IllegalArgumentException("Usuario no encontrado."));

        mockMvc.perform(get("/inicio/99"))
                .andExpect(status().isBadRequest()) // Verifica que el estado HTTP sea 400
                .andExpect(content().string("Usuario no encontrado.")); // Verifica el mensaje de error
    }

     */

}
