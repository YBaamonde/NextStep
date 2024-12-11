package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import com.nextstep.nextstepBackEnd.service.pdf.InformePdfService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private InformePdfService informePdfService;

    private ObjectMapper objectMapper;
    private SimulacionDTO simulacionDTO;

    @BeforeEach
    void setUp() {
        // Configuración del ObjectMapper para manejar Java Time
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Configuración del DTO de prueba
        Map<String, Map<String, Double>> gastosClasificados = new HashMap<>();
        gastosClasificados.put("esenciales", Map.of(
                "vivienda", 800.0,
                "alimentacion", 400.0,
                "transporte", 200.0
        ));
        gastosClasificados.put("opcionales", Map.of(
                "entretenimiento", 150.0,
                "suscripciones", 50.0
        ));

        simulacionDTO = new SimulacionDTO();
        simulacionDTO.setIngresos(2500.0);
        simulacionDTO.setMesesSimulacion(6);
        simulacionDTO.setMetaAhorro(1000.0);
        simulacionDTO.setGastosClasificados(gastosClasificados);
        simulacionDTO.setBalanceProyectado(950.0);
        simulacionDTO.setRecomendaciones(List.of(
                "Su balance proyectado es positivo. Está en una buena posición para independizarse.",
                "Considere reducir gastos opcionales en estas categorías: [entretenimiento, suscripciones]"
        ));
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testCalcularSimulacion() throws Exception {
        // Mock del servicio para calcular la simulación
        when(simulacionService.calcularSimulacion(any(SimulacionDTO.class))).thenReturn(simulacionDTO);

        mockMvc.perform(post("/simulacion/calcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(simulacionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balanceProyectado").value(950.0))
                .andExpect(jsonPath("$.recomendaciones[0]").value("Su balance proyectado es positivo. Está en una buena posición para independizarse."))
                .andExpect(jsonPath("$.recomendaciones[1]").value("Considere reducir gastos opcionales en estas categorías: [entretenimiento, suscripciones]"));
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testExportarSimulacionPdf() throws Exception {
        // Simulación de contenido PDF
        byte[] pdfContent = new byte[]{1, 2, 3, 4};

        // Mock del servicio de exportación a PDF
        when(informePdfService.generarPdfSimulacion(any(SimulacionDTO.class))).thenReturn(pdfContent);

        // Asegurar que el contenido PDF no sea null
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
