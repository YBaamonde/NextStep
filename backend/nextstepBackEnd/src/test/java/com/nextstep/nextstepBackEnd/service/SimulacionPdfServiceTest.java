package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SimulacionPdfServiceTest {

    @Autowired
    private SimulacionPdfService simulacionPdfService;

    private SimulacionDTO simulacionDTO;

    @BeforeEach
    void setUp() {
        // Inicializamos un DTO con datos de prueba
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
    void generarPdfSimulacion() {
        // Genera el PDF
        byte[] pdfBytes = simulacionPdfService.generarPdfSimulacion(simulacionDTO);

        // Verificamos que el PDF no sea nulo ni vacío
        assertNotNull(pdfBytes, "El PDF no debería ser nulo");
        assertTrue(pdfBytes.length > 0, "El PDF debería tener contenido");
    }
}
