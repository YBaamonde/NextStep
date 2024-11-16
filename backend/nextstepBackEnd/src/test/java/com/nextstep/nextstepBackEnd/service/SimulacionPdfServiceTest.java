package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SimulacionPdfServiceTest {

    @Autowired
    private SimulacionPdfService simulacionPdfService;

    private SimulacionDTO simulacionDTO;

    @BeforeEach
    void setUp() {
        // Inicializamos un DTO con datos de prueba
        Map<String, Map<String, Double>> gastosClasificados = new HashMap<>();
        gastosClasificados.put("esenciales", Map.of("vivienda", 800.0, "alimentacion", 400.0));
        gastosClasificados.put("opcionales", Map.of("transporte", 200.0, "entretenimiento", 150.0));

        simulacionDTO = new SimulacionDTO();
        simulacionDTO.setIngresos(2500.0);
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
        // Generar el PDF
        byte[] pdfBytes = simulacionPdfService.generarPdfSimulacion(simulacionDTO);

        // Validar que el PDF no sea nulo ni vacío
        assertNotNull(pdfBytes, "El PDF generado no debe ser nulo");
        assertTrue(pdfBytes.length > 0, "El PDF generado debe contener datos");

        // Validar que el PDF comienza con los bytes estándar "%PDF"
        String pdfHeader = new String(pdfBytes, 0, 4); // Leer los primeros 4 bytes del PDF
        assertEquals("%PDF", pdfHeader, "El archivo generado debería ser un PDF válido");

        // Validar que el tamaño del archivo es razonable (ejemplo: mayor que 100 bytes)
        assertTrue(pdfBytes.length > 100, "El PDF generado debería tener un tamaño razonable");

        // Mensaje de éxito
        System.out.println("El PDF se generó correctamente con tamaño: " + pdfBytes.length + " bytes.");
    }

}
