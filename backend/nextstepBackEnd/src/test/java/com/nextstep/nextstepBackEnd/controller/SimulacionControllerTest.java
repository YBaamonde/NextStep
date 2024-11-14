package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import com.nextstep.nextstepBackEnd.service.SimulacionService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SimulacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimulacionService simulacionService;

    private ObjectMapper objectMapper;
    private SimulacionDTO simulacionDTO;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        Map<String, Double> gastos = Map.of(
                "vivienda", 800.0,
                "alimentacion", 400.0,
                "transporte", 200.0,
                "entretenimiento", 150.0
        );

        Map<String, Map<String, Double>> gastosClasificados = new HashMap<>();
        gastosClasificados.put("esenciales", Map.of("vivienda", 800.0, "alimentacion", 400.0));
        gastosClasificados.put("opcionales", Map.of("transporte", 200.0, "entretenimiento", 150.0));

        simulacionDTO = SimulacionDTO.builder()
                .ingresos(2500.0)
                .gastos(gastos)
                .mesesSimulacion(6)
                .metaAhorro(1000.0)
                .gastosClasificados(gastosClasificados)
                .balanceProyectado(950.0)
                .balancePorMes(Map.of(1, 950.0, 2, 950.0, 3, 950.0, 4, 950.0, 5, 950.0, 6, 950.0))
                .recomendaciones(List.of(
                        "Su balance proyectado es positivo. Está en una buena posición para independizarse.",
                        "Considere reducir gastos opcionales en estas categorías: [transporte, entretenimiento]"
                ))
                .build();
    }

    @Test
    @WithMockUser(roles = "normal")  // Simula un usuario autenticado con rol normal
    public void testCalcularSimulacion() throws Exception {
        when(simulacionService.calcularSimulacion(simulacionDTO)).thenReturn(simulacionDTO);

        mockMvc.perform(post("/simulacion/calcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(simulacionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balanceProyectado").value(950.0))
                .andExpect(jsonPath("$.recomendaciones[0]").value("Su balance proyectado es positivo. Está en una buena posición para independizarse."))
                .andExpect(jsonPath("$.recomendaciones[1]").value("Considere reducir gastos opcionales en estas categorías: [transporte, entretenimiento]"));
    }
}
