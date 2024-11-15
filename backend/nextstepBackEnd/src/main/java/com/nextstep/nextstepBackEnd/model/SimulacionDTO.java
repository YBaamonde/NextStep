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

    private double ingresos;
    private int mesesSimulacion;
    private Double metaAhorro;
    private double balanceProyectado;

    // Cambiar Map<String, Double> gastos por gastosClasificados para ajustar a la estructura JSON
    private Map<String, Map<String, Double>> gastosClasificados = new HashMap<>();

    private Map<Integer, Double> balancePorMes = new HashMap<>();
    private List<String> recomendaciones = new ArrayList<>();
}
