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
                .build();
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

        // Verifica que cada mes tiene el balance proyectado correcto
        assertNotNull(balancePorMes);
        assertEquals(6, balancePorMes.size());
        for (int mes = 1; mes <= simulacionDTO.getMesesSimulacion(); mes++) {
            assertEquals(950.0, balancePorMes.get(mes));
        }
    }


    @Test
    void evaluarMetaAhorro_MetaAlcanzable() {
        // Configura una simulación en la que se puede alcanzar la meta de ahorro
        simulacionDTO.setMetaAhorro(1000.0);
        Map<Integer, Double> balancePorMes = Map.of(1, 950.0, 2, 950.0, 3, 950.0, 4, 950.0, 5, 950.0, 6, 950.0);

        simulacionService.evaluarMetaAhorro(simulacionDTO, balancePorMes);

        assertNotNull(simulacionDTO.getRecomendaciones());
        assertEquals("Meta de ahorro alcanzable con los ingresos y gastos actuales.", simulacionDTO.getRecomendaciones().get(0));
    }

    @Test
    void evaluarMetaAhorro_MetaNoAlcanzable() {
        // Configura una meta que es apenas superior al ahorro acumulado
        simulacionDTO.setMetaAhorro(6000.0);
        Map<Integer, Double> balancePorMes = Map.of(1, 950.0, 2, 950.0, 3, 950.0, 4, 950.0, 5, 950.0, 6, 950.0);

        simulacionService.evaluarMetaAhorro(simulacionDTO, balancePorMes);

        // El déficit real calculado debería ser 300.0
        assertNotNull(simulacionDTO.getRecomendaciones());
        assertEquals("Para alcanzar su meta de ahorro, necesita reducir sus gastos o aumentar sus ingresos en: 300.0", simulacionDTO.getRecomendaciones().get(0));
    }



    @Test
    void generarRecomendaciones() {
        simulacionDTO.setBalanceProyectado(950.0); // Balance positivo
        List<String> recomendaciones = simulacionService.generarRecomendaciones(simulacionDTO, simulacionDTO.getBalanceProyectado());

        assertNotNull(recomendaciones);
        assertEquals(2, recomendaciones.size());
        assertEquals("Su balance proyectado es positivo. Está en una buena posición para independizarse.", recomendaciones.get(0));

        // Verifica que la recomendación contenga las categorías opcionales sin importar el orden
        String recomendacionOpcionales = "Considere reducir gastos opcionales en estas categorías: [transporte, entretenimiento]";
        assertTrue(recomendaciones.get(1).contains("Considere reducir gastos opcionales en estas categorías"));
        assertTrue(recomendaciones.get(1).contains("transporte"));
        assertTrue(recomendaciones.get(1).contains("entretenimiento"));
    }


    @Test
    void calcularSimulacionCompleta() {
        // Ejecuta la simulación completa en `calcularSimulacion`
        SimulacionDTO resultado = simulacionService.calcularSimulacion(simulacionDTO);

        // Verifica el balance proyectado
        assertNotNull(resultado);
        assertEquals(950.0, resultado.getBalanceProyectado());

        // Verifica el balance mes a mes
        assertEquals(6, resultado.getBalancePorMes().size());
        assertEquals(950.0, resultado.getBalancePorMes().get(1)); // Primer mes

        // Verifica recomendaciones
        assertNotNull(resultado.getRecomendaciones());
        assertEquals("Su balance proyectado es positivo. Está en una buena posición para independizarse.", resultado.getRecomendaciones().get(0));

        // Verifica la presencia de categorías opcionales sin importar el orden
        String recomendacionOpcionales = resultado.getRecomendaciones().get(1);
        assertTrue(recomendacionOpcionales.contains("Considere reducir gastos opcionales en estas categorías"));
        assertTrue(recomendacionOpcionales.contains("transporte"));
        assertTrue(recomendacionOpcionales.contains("entretenimiento"));
    }

}