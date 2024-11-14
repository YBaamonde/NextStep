package com.nextstep.nextstepBackEnd.service;


import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimulacionService {

    // Calcula el balance proyectado basado en ingresos y gastos simulados
    public SimulacionDTO calcularSimulacion(SimulacionDTO simulacionDTO) {
        double ingresos = simulacionDTO.getIngresos();  // Obtener ingresos simulados
        double totalGastos = calcularTotalGastos(simulacionDTO);  // Sumar los gastos simulados

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

    // Suma todos los valores en el mapa de gastos del DTO
    double calcularTotalGastos(SimulacionDTO simulacionDTO) {
        return simulacionDTO.getGastos().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    // Calcula el balance mes a mes durante el período de simulación
    Map<Integer, Double> calcularBalanceMensual(SimulacionDTO simulacionDTO, double ingresos, double totalGastos) {
        Map<Integer, Double> balancePorMes = new HashMap<>();
        for (int mes = 1; mes <= simulacionDTO.getMesesSimulacion(); mes++) {
            balancePorMes.put(mes, ingresos - totalGastos);
        }
        return balancePorMes;
    }

    // Evalúa si el usuario puede alcanzar su meta de ahorro en el período de simulación
    void evaluarMetaAhorro(SimulacionDTO simulacionDTO, Map<Integer, Double> balancePorMes) {
        // Inicializar la lista de recomendaciones si es null
        if (simulacionDTO.getRecomendaciones() == null) {
            simulacionDTO.setRecomendaciones(new ArrayList<>());
        }

        double ahorroAcumulado = balancePorMes.values().stream().mapToDouble(Double::doubleValue).sum();
        double metaAhorro = simulacionDTO.getMetaAhorro();

        if (ahorroAcumulado >= metaAhorro) {
            simulacionDTO.getRecomendaciones().add("Meta de ahorro alcanzable con los ingresos y gastos actuales.");
        } else {
            double deficit = metaAhorro - ahorroAcumulado;
            simulacionDTO.getRecomendaciones().add("Para alcanzar su meta de ahorro, necesita reducir sus gastos o aumentar sus ingresos en: " + deficit);
        }
    }


    // Genera recomendaciones en función de los gastos clasificados y el balance proyectado
    List<String> generarRecomendaciones(SimulacionDTO simulacionDTO, double balanceProyectado) {
        List<String> recomendaciones = new ArrayList<>();

        if (balanceProyectado < 0) {
            recomendaciones.add("Su balance proyectado es negativo. Considere reducir sus gastos.");
        } else {
            recomendaciones.add("Su balance proyectado es positivo. Está en una buena posición para independizarse.");
        }

        Map<String, Double> gastosEsenciales = simulacionDTO.getGastosClasificados().get("esenciales");
        Map<String, Double> gastosOpcionales = simulacionDTO.getGastosClasificados().get("opcionales");

        if (gastosOpcionales != null) {
            double totalOpcionales = gastosOpcionales.values().stream().mapToDouble(Double::doubleValue).sum();
            if (totalOpcionales > 0) {
                recomendaciones.add("Considere reducir gastos opcionales en estas categorías: " + gastosOpcionales.keySet());
            }
        }

        return recomendaciones;
    }
}
