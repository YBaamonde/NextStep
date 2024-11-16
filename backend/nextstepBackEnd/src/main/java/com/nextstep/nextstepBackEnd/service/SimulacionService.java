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
        System.out.println("Datos recibidos en el backend: " + simulacionDTO); // DEBUG

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
        double ahorroAcumulado = balanceProyectado * simulacionDTO.getMesesSimulacion();
        Double metaAhorro = simulacionDTO.getMetaAhorro();

        if (balanceProyectado < 0) {
            recomendaciones.add("Su balance proyectado es negativo. Reduzca gastos esenciales o considere incrementar sus ingresos.");
        } else {
            recomendaciones.add("Su balance proyectado es positivo. Puede considerar ahorrar o invertir.");
        }

        if (metaAhorro != null && metaAhorro > 0) {
            if (ahorroAcumulado >= metaAhorro) {
                recomendaciones.add("¡Felicitaciones! Su meta de ahorro está alcanzada en " + simulacionDTO.getMesesSimulacion() + " meses.");
            } else {
                double deficit = metaAhorro - ahorroAcumulado;
                recomendaciones.add("Para alcanzar su meta de ahorro de " + metaAhorro + " €, necesita ahorrar " + deficit + " € más.");
            }
        }

        Map<String, Double> gastosOpcionales = simulacionDTO.getGastosClasificados().get("opcionales");
        if (gastosOpcionales != null && !gastosOpcionales.isEmpty()) {
            recomendaciones.add("Puede reducir gastos opcionales como: " + String.join(", ", gastosOpcionales.keySet()));
        }

        return recomendaciones;
    }



}
