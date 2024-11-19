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
        System.out.println("Datos recibidos en el backend: " + simulacionDTO); // Debug

        if (simulacionDTO.getIngresos() < 0 || simulacionDTO.getMesesSimulacion() <= 0) {
            simulacionDTO.getRecomendaciones().add("Los ingresos y la duración de la simulación deben ser positivos.");
            simulacionDTO.setBalanceProyectado(0);
            System.out.println("Simulación detenida por datos negativos o inválidos."); // Debug
            return simulacionDTO;
        }

        double ingresos = simulacionDTO.getIngresos();
        double totalGastos = calcularTotalDeGastosClasificados(simulacionDTO.getGastosClasificados());
        System.out.println("Total de gastos calculados: " + totalGastos); // Debug

        calcularProporcionGastos(simulacionDTO, ingresos);

        double balanceProyectado = ingresos - totalGastos;
        simulacionDTO.setBalanceProyectado(balanceProyectado);
        System.out.println("Balance proyectado calculado: " + balanceProyectado); // Debug

        Map<Integer, Double> balancePorMes = calcularBalanceMensual(simulacionDTO, ingresos, totalGastos);
        simulacionDTO.setBalancePorMes(balancePorMes);
        System.out.println("Balance mensual calculado: " + balancePorMes); // Debug

        if (simulacionDTO.getMetaAhorro() != null) {
            evaluarMetaAhorro(simulacionDTO, balancePorMes);
        }

        List<String> recomendaciones = generarRecomendaciones(simulacionDTO, balanceProyectado);
        simulacionDTO.setRecomendaciones(recomendaciones);
        System.out.println("Recomendaciones generadas: " + recomendaciones); // Debug

        System.out.println("Simulación finalizada con éxito: " + simulacionDTO); // Debug
        return simulacionDTO;
    }


    // Metodo para calcular el total de todos los gastos clasificados (esenciales y opcionales)
    double calcularTotalDeGastosClasificados(Map<String, Map<String, Double>> gastosClasificados) {
        if (gastosClasificados == null) return 0.0;
        return gastosClasificados.values().stream()
                .flatMap(gastos -> gastos.values().stream())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    // Metodo para calcular el total de un submapa específico (esenciales u opcionales)
    private double calcularTotalGastos(Map<String, Double> gastos) {
        if (gastos == null) return 0.0;
        return gastos.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    void calcularProporcionGastos(SimulacionDTO simulacionDTO, double ingresos) {
        Map<String, Map<String, Double>> gastosClasificados = simulacionDTO.getGastosClasificados();
        if (gastosClasificados == null) return;

        double totalEsenciales = calcularTotalGastos(gastosClasificados.get("esenciales"));
        double totalOpcionales = calcularTotalGastos(gastosClasificados.get("opcionales"));

        double proporcionEsenciales = ingresos > 0 ? (totalEsenciales / ingresos) * 100 : 0;
        double proporcionOpcionales = ingresos > 0 ? (totalOpcionales / ingresos) * 100 : 0;

        simulacionDTO.getProporciones().put("esenciales", proporcionEsenciales);
        simulacionDTO.getProporciones().put("opcionales", proporcionOpcionales);

        System.out.println("Proporciones calculadas: Esenciales = " + proporcionEsenciales + ", Opcionales = " + proporcionOpcionales);
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
            recomendaciones.add("Su balance proyectado es negativo. Reduzca gastos esenciales o considere incrementar sus ingresos.");
        } else {
            recomendaciones.add("Su balance proyectado es positivo. Puede considerar ahorrar o invertir.");
        }

        if (simulacionDTO.getMetaAhorro() != null && simulacionDTO.getMetaAhorro() > 0) {
            double deficit = simulacionDTO.getMetaAhorro() - balanceProyectado * simulacionDTO.getMesesSimulacion();
            if (deficit > 0) {
                recomendaciones.add("Para alcanzar su meta de ahorro de " + simulacionDTO.getMetaAhorro() +
                        " €, necesita ahorrar " + deficit + " € más.");
            } else {
                recomendaciones.add("¡Felicitaciones! Su meta de ahorro está alcanzada en " +
                        simulacionDTO.getMesesSimulacion() + " meses.");
            }
        }

        Map<String, Double> gastosOpcionales = simulacionDTO.getGastosClasificados().get("opcionales");
        if (gastosOpcionales != null && !gastosOpcionales.isEmpty()) {
            recomendaciones.add("Puede reducir gastos opcionales como: " +
                    String.join(", ", gastosOpcionales.keySet()));
        }

        return recomendaciones;
    }
}
