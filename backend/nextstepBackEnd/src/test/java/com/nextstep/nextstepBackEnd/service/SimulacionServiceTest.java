package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
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
                "Vivienda", 800.0,
                "Alimentación", 100.0,
                "Transporte", 100.0
        ));
        gastosClasificados.put("opcionales", Map.of(
                "Entretenimiento", 400.0,
                "Suscripciones", 100.0
        ));

        simulacionDTO = new SimulacionDTO();
        simulacionDTO.setIngresos(2000.0);
        simulacionDTO.setGastosClasificados(gastosClasificados);
        simulacionDTO.setMesesSimulacion(6);
        simulacionDTO.setMetaAhorro(1000.0);
    }

    @Test
    void calcularProporciones() {
        // Act
        simulacionService.calcularProporcionGastos(simulacionDTO, simulacionDTO.getIngresos());

        // Assert
        assertNotNull(simulacionDTO.getProporciones(), "Las proporciones no deben ser nulas");
        assertEquals(50.0, simulacionDTO.getProporciones().get("esenciales"), 0.01, "La proporción de esenciales debe ser 56.0%");
        assertEquals(25.0, simulacionDTO.getProporciones().get("opcionales"), 0.01, "La proporción de opcionales debe ser 8.0%");
    }



    @Test
    void calcularBalanceMensual() {
        // Arrange
        double ingresos = simulacionDTO.getIngresos();
        double totalGastos = simulacionService.calcularTotalDeGastosClasificados(simulacionDTO.getGastosClasificados());

        // Act
        Map<Integer, Double> balancePorMes = simulacionService.calcularBalanceMensual(simulacionDTO, ingresos, totalGastos);

        // Assert
        assertNotNull(balancePorMes, "El balance mensual no debe ser nulo");
        assertEquals(6, balancePorMes.size(), "Debe calcularse el balance para 6 meses");

        for (int mes = 1; mes <= simulacionDTO.getMesesSimulacion(); mes++) {
            assertEquals(500.0, balancePorMes.get(mes), "El balance mensual debe ser 500.0 (2000 - 1500)");
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

        simulacionDTO.setBalanceProyectado(-100.0); // Balance negativo
        recomendaciones = simulacionService.generarRecomendaciones(simulacionDTO, simulacionDTO.getBalanceProyectado());
        assertTrue(recomendaciones.get(0).contains("Su balance proyectado es negativo"));
    }

    @Test
    void calcularSimulacionCompleta() {
        // Act
        SimulacionDTO resultado = simulacionService.calcularSimulacion(simulacionDTO);

        // Assert
        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(500.0, resultado.getBalanceProyectado(), "El balance proyectado debe ser correcto");
        assertNotNull(resultado.getBalancePorMes(), "El balance por mes no debe ser nulo");
        assertEquals(6, resultado.getBalancePorMes().size(), "Debe calcularse el balance para cada mes");
        assertNotNull(resultado.getRecomendaciones(), "Las recomendaciones no deben ser nulas");
        assertTrue(resultado.getRecomendaciones().size() >= 1, "Debe generar al menos una recomendación");
    }

}
