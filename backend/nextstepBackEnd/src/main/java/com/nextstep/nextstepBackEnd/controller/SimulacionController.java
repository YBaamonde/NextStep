package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import com.nextstep.nextstepBackEnd.service.SimulacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulacion")
public class SimulacionController {

    private final SimulacionService simulacionService;

    public SimulacionController(SimulacionService simulacionService) {
        this.simulacionService = simulacionService;
    }

    // Endpoint para calcular la simulación financiera avanzada basada en los datos del usuario
    // Se usa un solo enpoint para asegurar la consistencia de los resultados
    @PostMapping("/calcular")
    public ResponseEntity<SimulacionDTO> calcularSimulacion(@RequestBody SimulacionDTO simulacionDTO) {
        // Llama a SimulacionService para procesar y calcular todos los datos de simulación
        SimulacionDTO resultadoSimulacion = simulacionService.calcularSimulacion(simulacionDTO);
        return ResponseEntity.ok(resultadoSimulacion);
    }
}