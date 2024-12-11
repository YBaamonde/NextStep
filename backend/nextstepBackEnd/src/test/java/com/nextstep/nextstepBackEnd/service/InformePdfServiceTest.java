package com.nextstep.nextstepBackEnd.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import com.nextstep.nextstepBackEnd.service.pdf.InformePdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class InformePdfServiceTest {

    @Autowired
    private InformePdfService informePdfService;

    private SimulacionDTO simulacionDTO;
    private Map<String, Double> evolucionTrimestral;

    @BeforeEach
    void setUp() {
        // Datos para prueba de simulación
        Map<String, Map<String, Double>> gastosClasificados = new HashMap<>();
        gastosClasificados.put("Esenciales", Map.of("Vivienda", 800.0, "Alimentación", 400.0));
        gastosClasificados.put("Opcionales", Map.of("Transporte", 200.0, "Entretenimiento", 150.0));

        simulacionDTO = new SimulacionDTO();
        simulacionDTO.setIngresos(2500.0);
        simulacionDTO.setMesesSimulacion(6);
        simulacionDTO.setMetaAhorro(1000.0);
        simulacionDTO.setBalanceProyectado(950.0);
        simulacionDTO.setGastosClasificados(gastosClasificados);
        simulacionDTO.setProporciones(Map.of("Esenciales", 60.0, "Opcionales", 40.0));
        simulacionDTO.setRecomendaciones(List.of(
                "Reduzca gastos opcionales en transporte y entretenimiento.",
                "Considere ahorrar un 20% de sus ingresos."
        ));

        // Datos para prueba de evolución trimestral
        evolucionTrimestral = new HashMap<>();
        evolucionTrimestral.put("Q1", 1500.0);
        evolucionTrimestral.put("Q2", 1200.0);
        evolucionTrimestral.put("Q3", 1800.0);
        evolucionTrimestral.put("Q4", 2000.0);
    }

    @Test
    void generarPdfSimulacion_ShouldGenerateValidPdf() {
        byte[] pdfBytes = informePdfService.generarPdfSimulacion(simulacionDTO);

        assertNotNull(pdfBytes, "El PDF generado no debería ser nulo.");
        assertTrue(pdfBytes.length > 0, "El PDF generado debería contener datos.");

        // Validar que el PDF comienza con los bytes estándar "%PDF"
        String pdfHeader = new String(pdfBytes, 0, 4);
        assertEquals("%PDF", pdfHeader, "El archivo generado debería ser un PDF válido.");
    }

    @Test
    void generarPdfSimulacion_InvalidData_ShouldThrowException() {
        // Configurar un DTO inválido
        SimulacionDTO simulacionInvalida = new SimulacionDTO();
        simulacionInvalida.setIngresos(0.0); // Ingresos inválidos
        simulacionInvalida.setMesesSimulacion(0); // Meses inválidos
        simulacionInvalida.setGastosClasificados(new HashMap<>()); // Vacío

        // Verificar que se lanza una excepción
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> informePdfService.generarPdfSimulacion(simulacionInvalida));
        assertEquals("Datos inválidos para generar el PDF de simulación", exception.getMessage());
    }

    @Test
    void generarPdfEvolucionTrimestral_ShouldGenerateValidPdf() {
        byte[] pdfBytes = informePdfService.generarPdfEvolucionTrimestral(evolucionTrimestral);

        assertNotNull(pdfBytes, "El PDF generado no debería ser nulo.");
        assertTrue(pdfBytes.length > 0, "El PDF generado debería contener datos.");

        // Validar que el PDF comienza con los bytes estándar "%PDF"
        String pdfHeader = new String(pdfBytes, 0, 4);
        assertEquals("%PDF", pdfHeader, "El archivo generado debería ser un PDF válido.");
    }

    @Test
    void generarPdfEvolucionTrimestral_InvalidData_ShouldThrowException() {
        // Configurar un mapa inválido
        Map<String, Double> evolucionInvalida = new HashMap<>(); // Vacío

        // Verificar que se lanza una excepción
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> informePdfService.generarPdfEvolucionTrimestral(evolucionInvalida));
        assertEquals("Datos inválidos para generar el PDF de evolución trimestral", exception.getMessage());
    }
}
