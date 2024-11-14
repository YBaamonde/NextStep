package com.nextstep.nextstepBackEnd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class SimulacionDTO {

    private double ingresos;
    private Map<String, Double> gastos;
    private double balanceProyectado;
    private int mesesSimulacion;
    private Double metaAhorro;
    private Map<String, Map<String, Double>> gastosClasificados;
    private Map<Integer, Double> balancePorMes;

    @Builder.Default
    private List<String> recomendaciones = new ArrayList<>();
}