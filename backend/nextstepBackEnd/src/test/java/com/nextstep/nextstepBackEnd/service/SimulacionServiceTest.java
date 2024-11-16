package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import com.nextstep.nextstepBackEnd.service.SimulacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SimulacionServiceTest {

    @InjectMocks
    private SimulacionService simulacionService;

    private SimulacionDTO simulacionDTO;

    @BeforeEach
    void setUp() {
        // Configurar DTO con datos clasificados (esenciales y opcionales)
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
        simulacionDTO.setGastosClasificados(gastosClasificados);
        simulacionDTO.setMesesSimulacion(6);
        simulacionDTO.setMetaAhorro(1000.0);
    }

    @Test
    void calcularTotalGastos() {
        // Act
        double totalGastos = simulacionService.calcularTotalGastos(simulacionDTO.getGastosClasificados());

        // Assert
        assertEquals(1600.0, totalGastos); // 800 + 400 + 200 + 150 + 50 = 1600
    }

    @Test
    void calcularBalanceMensual() {
        // Arrange
        double ingresos = simulacionDTO.getIngresos();
        double totalGastos = simulacionService.calcularTotalGastos(simulacionDTO.getGastosClasificados());

        // Act
        Map<Integer, Double> balancePorMes = simulacionService.calcularBalanceMensual(simulacionDTO, ingresos, totalGastos);

        // Assert
        assertNotNull(balancePorMes, "El balance mensual no debe ser nulo");
        assertEquals(6, balancePorMes.size(), "Debe calcularse el balance para 6 meses");

        for (int mes = 1; mes <= simulacionDTO.getMesesSimulacion(); mes++) {
            assertEquals(900.0, balancePorMes.get(mes), "El balance mensual debe ser 900.0 (2500 - 1600)");
        }
    }

    @Test
    void evaluarMetaAhorro_MetaAlcanzable() {
        // Arrange
        Map<Integer, Double> balancePorMes = Map.of(1, 900.0, 2, 900.0, 3, 900.0, 4, 900.0, 5, 900.0, 6, 900.0);

        // Act
        simulacionService.evaluarMetaAhorro(simulacionDTO, balancePorMes);

        // Assert
        assertNotNull(simulacionDTO.getRecomendaciones());
        assertTrue(simulacionDTO.getRecomendaciones().get(0).contains("Meta de ahorro alcanzable"));
    }

    @Test
    void evaluarMetaAhorro_MetaNoAlcanzable() {
        // Arrange
        simulacionDTO.setMetaAhorro(7000.0); // Meta más alta que el ahorro posible
        Map<Integer, Double> balancePorMes = Map.of(1, 900.0, 2, 900.0, 3, 900.0, 4, 900.0, 5, 900.0, 6, 900.0);

        // Act
        simulacionService.evaluarMetaAhorro(simulacionDTO, balancePorMes);

        // Assert
        assertNotNull(simulacionDTO.getRecomendaciones());
        assertTrue(simulacionDTO.getRecomendaciones().get(0).contains("necesita reducir sus gastos o aumentar sus ingresos"));
    }

    @Test
    void generarRecomendaciones() {
        // Arrange
        simulacionDTO.setBalanceProyectado(900.0);

        // Act
        List<String> recomendaciones = simulacionService.generarRecomendaciones(simulacionDTO, simulacionDTO.getBalanceProyectado());

        // Assert
        assertNotNull(recomendaciones, "Las recomendaciones no deben ser nulas");
        assertTrue(recomendaciones.get(0).contains("Su balance proyectado es positivo"));
        assertTrue(recomendaciones.size() >= 1);

        if (simulacionDTO.getBalanceProyectado() < 0) {
            assertTrue(recomendaciones.get(1).contains("Considere reducir gastos opcionales"));
        }
    }

    @Test
    void calcularSimulacionCompleta() {
        // Act
        SimulacionDTO resultado = simulacionService.calcularSimulacion(simulacionDTO);

        // Assert
        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(900.0, resultado.getBalanceProyectado(), "El balance proyectado debe ser correcto");
        assertNotNull(resultado.getBalancePorMes(), "El balance por mes no debe ser nulo");
        assertEquals(6, resultado.getBalancePorMes().size(), "Debe calcularse el balance para cada mes");
        assertNotNull(resultado.getRecomendaciones(), "Las recomendaciones no deben ser nulas");
        assertTrue(resultado.getRecomendaciones().size() >= 1, "Debe generar al menos una recomendación");
    }
}
