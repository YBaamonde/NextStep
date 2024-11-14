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

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SimulacionServiceTest {

    @InjectMocks
    private SimulacionService simulacionService;

    private SimulacionDTO simulacionDTO;

    @BeforeEach
    void setUp() {
        simulacionDTO = new SimulacionDTO();
        simulacionDTO.setIngresos(2500.0);
        simulacionDTO.setGastos(Map.of(
                "vivienda", 800.0,
                "alimentacion", 400.0,
                "transporte", 200.0,
                "entretenimiento", 150.0
        ));
        simulacionDTO.setMesesSimulacion(6);
        simulacionDTO.setMetaAhorro(1000.0);
        simulacionDTO.setGastosClasificados(new HashMap<>(Map.of(
                "esenciales", Map.of("vivienda", 800.0, "alimentacion", 400.0),
                "opcionales", Map.of("transporte", 200.0, "entretenimiento", 150.0)
        )));
    }

    @Test
    void calcularTotalGastos() {
        double totalGastos = simulacionService.calcularTotalGastos(simulacionDTO);
        assertEquals(1550.0, totalGastos); // 800 + 400 + 200 + 150 = 1550
    }

    @Test
    void calcularBalanceMensual() {
        double ingresos = simulacionDTO.getIngresos();
        double totalGastos = simulacionService.calcularTotalGastos(simulacionDTO);

        Map<Integer, Double> balancePorMes = simulacionService.calcularBalanceMensual(simulacionDTO, ingresos, totalGastos);

        assertNotNull(balancePorMes);
        assertEquals(6, balancePorMes.size());
        for (int mes = 1; mes <= simulacionDTO.getMesesSimulacion(); mes++) {
            assertEquals(950.0, balancePorMes.get(mes));
        }
    }

    @Test
    void evaluarMetaAhorro_MetaAlcanzable() {
        simulacionDTO.setMetaAhorro(1000.0);
        Map<Integer, Double> balancePorMes = Map.of(1, 950.0, 2, 950.0, 3, 950.0, 4, 950.0, 5, 950.0, 6, 950.0);

        simulacionService.evaluarMetaAhorro(simulacionDTO, balancePorMes);

        assertNotNull(simulacionDTO.getRecomendaciones());
        assertEquals("Meta de ahorro alcanzable con los ingresos y gastos actuales.", simulacionDTO.getRecomendaciones().get(0));
    }

    @Test
    void evaluarMetaAhorro_MetaNoAlcanzable() {
        simulacionDTO.setMetaAhorro(6000.0);
        Map<Integer, Double> balancePorMes = Map.of(1, 950.0, 2, 950.0, 3, 950.0, 4, 950.0, 5, 950.0, 6, 950.0);

        simulacionService.evaluarMetaAhorro(simulacionDTO, balancePorMes);

        assertNotNull(simulacionDTO.getRecomendaciones());
        assertEquals("Para alcanzar su meta de ahorro, necesita reducir sus gastos o aumentar sus ingresos en: 300.0", simulacionDTO.getRecomendaciones().get(0));
    }

    @Test
    void generarRecomendaciones() {
        simulacionDTO.setBalanceProyectado(950.0);
        List<String> recomendaciones = simulacionService.generarRecomendaciones(simulacionDTO, simulacionDTO.getBalanceProyectado());

        assertNotNull(recomendaciones);
        assertEquals(2, recomendaciones.size());
        assertEquals("Su balance proyectado es positivo. Está en una buena posición para independizarse.", recomendaciones.get(0));
        assertTrue(recomendaciones.get(1).contains("Considere reducir gastos opcionales en estas categorías"));
        assertTrue(recomendaciones.get(1).contains("transporte"));
        assertTrue(recomendaciones.get(1).contains("entretenimiento"));
    }

    @Test
    void calcularSimulacionCompleta() {
        SimulacionDTO resultado = simulacionService.calcularSimulacion(simulacionDTO);

        assertNotNull(resultado);
        assertEquals(950.0, resultado.getBalanceProyectado());
        assertEquals(6, resultado.getBalancePorMes().size());
        assertEquals(950.0, resultado.getBalancePorMes().get(1));

        assertNotNull(resultado.getRecomendaciones());
        assertEquals("Su balance proyectado es positivo. Está en una buena posición para independizarse.", resultado.getRecomendaciones().get(0));

        String recomendacionOpcionales = resultado.getRecomendaciones().get(1);
        assertTrue(recomendacionOpcionales.contains("Considere reducir gastos opcionales en estas categorías"));
        assertTrue(recomendacionOpcionales.contains("transporte"));
        assertTrue(recomendacionOpcionales.contains("entretenimiento"));
    }
}
