package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import com.nextstep.nextstepBackEnd.service.SimulacionPdfService;
import com.nextstep.nextstepBackEnd.service.SimulacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SimulacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimulacionService simulacionService;

    @MockBean
    private SimulacionPdfService simulacionPdfService;

    private ObjectMapper objectMapper;
    private SimulacionDTO simulacionDTO;

    @BeforeEach
    void setUp() {
        // Inicializa el ObjectMapper manualmente
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

        simulacionDTO = new SimulacionDTO();
        simulacionDTO.setIngresos(2500.0);
        simulacionDTO.setGastos(gastos);
        simulacionDTO.setMesesSimulacion(6);
        simulacionDTO.setMetaAhorro(1000.0);
        simulacionDTO.setBalanceProyectado(950.0);
        simulacionDTO.setGastosClasificados(gastosClasificados);
        simulacionDTO.setRecomendaciones(List.of(
                "Su balance proyectado es positivo. Está en una buena posición para independizarse.",
                "Considere reducir gastos opcionales en estas categorías: [transporte, entretenimiento]"
        ));
    }

    @Test
    @WithMockUser(roles = "normal")
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

    @Test
    @WithMockUser(roles = "normal")
    public void testExportarSimulacionPdf() throws Exception {
        // Crea un byte array simulado para el PDF
        byte[] pdfContent = new byte[]{1, 2, 3, 4}; // Simulación de contenido PDF

        // Configura el mock de SimulacionPdfService para devolver el contenido PDF
        when(simulacionPdfService.generarPdfSimulacion(any(SimulacionDTO.class))).thenReturn(pdfContent);

        // Asegúrate de que el contenido PDF simulado no sea null
        assertNotNull(pdfContent, "El contenido PDF simulado no debe ser null");

        mockMvc.perform(post("/simulacion/exportar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(simulacionDTO)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=simulacion.pdf"))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE))
                .andExpect(content().bytes(pdfContent));  // Verifica que el contenido del PDF sea el simulado
    }
}
