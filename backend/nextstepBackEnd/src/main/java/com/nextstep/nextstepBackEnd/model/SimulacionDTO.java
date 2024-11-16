package com.nextstep.nextstepBackEnd.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class SimulacionDTO {

    private double ingresos; // Ingresos mensuales
    private int mesesSimulacion; // Duración en meses de la simulación
    private Double metaAhorro; // Meta de ahorro deseada
    private double balanceProyectado; // Balance proyectado al final de la simulación

    private Map<String, Map<String, Double>> gastosClasificados = new HashMap<>(); // Gastos clasificados en esenciales y opcionales
    private Map<Integer, Double> balancePorMes = new HashMap<>(); // Balance mensual proyectado
    private List<String> recomendaciones = new ArrayList<>(); // Recomendaciones generadas durante la simulación
}
