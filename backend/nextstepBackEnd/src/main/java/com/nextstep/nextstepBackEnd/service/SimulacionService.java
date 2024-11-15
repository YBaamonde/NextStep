package com.nextstep.nextstepBackEnd.service;


import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimulacionService {

    public SimulacionDTO calcularSimulacion(SimulacionDTO simulacionDTO) {
        double ingresos = simulacionDTO.getIngresos();
        double totalGastos = calcularTotalGastos(simulacionDTO.getGastosClasificados()); // Ajuste para usar gastosClasificados

        // Calcular balance general proyectado
        double balanceProyectado = ingresos - totalGastos;
        simulacionDTO.setBalanceProyectado(balanceProyectado);

        // Calcular balance mes a mes
        Map<Integer, Double> balancePorMes = calcularBalanceMensual(simulacionDTO, ingresos, totalGastos);
        simulacionDTO.setBalancePorMes(balancePorMes);

        // Evaluar meta de ahorro
        if (simulacionDTO.getMetaAhorro() != null) {
            evaluarMetaAhorro(simulacionDTO, balancePorMes);
        }

        // Generar recomendaciones en base a los gastos clasificados
        List<String> recomendaciones = generarRecomendaciones(simulacionDTO, balanceProyectado);
        simulacionDTO.setRecomendaciones(recomendaciones);

        return simulacionDTO;
    }


    double calcularTotalGastos(Map<String, Map<String, Double>> gastosClasificados) {
        return gastosClasificados.values().stream()
                .flatMap(map -> map.values().stream())
                .mapToDouble(Double::doubleValue)
                .sum();
    }



    Map<Integer, Double> calcularBalanceMensual(SimulacionDTO simulacionDTO, double ingresos, double totalGastos) {
        Map<Integer, Double> balancePorMes = new HashMap<>();
        for (int mes = 1; mes <= simulacionDTO.getMesesSimulacion(); mes++) {
            balancePorMes.put(mes, ingresos - totalGastos);
        }
        return balancePorMes;
    }

    void evaluarMetaAhorro(SimulacionDTO simulacionDTO, Map<Integer, Double> balancePorMes) {
        double ahorroAcumulado = balancePorMes.values().stream().mapToDouble(Double::doubleValue).sum();
        double metaAhorro = simulacionDTO.getMetaAhorro();

        if (ahorroAcumulado >= metaAhorro) {
            simulacionDTO.getRecomendaciones().add("\nMeta de ahorro alcanzable con los ingresos y gastos actuales.");
        } else {
            double deficit = metaAhorro - ahorroAcumulado;
            simulacionDTO.getRecomendaciones().add("\nPara alcanzar su meta de ahorro, necesita reducir sus gastos o aumentar sus ingresos en: " + deficit);
        }
    }

    List<String> generarRecomendaciones(SimulacionDTO simulacionDTO, double balanceProyectado) {
        List<String> recomendaciones = new ArrayList<>();

        if (balanceProyectado < 0) {
            recomendaciones.add("\nSu balance proyectado es negativo. Considere reducir sus gastos.");
        } else {
            recomendaciones.add("\nSu balance proyectado es positivo. Está en una buena posición para independizarse.");
        }

        Map<String, Double> gastosOpcionales = simulacionDTO.getGastosClasificados().get("opcionales");
        if (gastosOpcionales != null && !gastosOpcionales.isEmpty() && balanceProyectado < 0) {
            recomendaciones.add("\nConsidere reducir gastos opcionales en estas categorías: " + gastosOpcionales.keySet());
        }

        return recomendaciones;
    }
}
