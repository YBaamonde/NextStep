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
    private Map<String, Double> gastos = new HashMap<>();
    private double balanceProyectado;
    private int mesesSimulacion;
    private Double metaAhorro;
    private Map<String, Map<String, Double>> gastosClasificados = new HashMap<>();  // Inicializar el mapa aquí
    private Map<Integer, Double> balancePorMes = new HashMap<>();
    private List<String> recomendaciones = new ArrayList<>();  // Inicializar también para evitar null
}