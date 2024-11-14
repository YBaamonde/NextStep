package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import com.nextstep.nextstepBackEnd.service.SimulacionPdfService;
import com.nextstep.nextstepBackEnd.service.SimulacionService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    // Endpoint para calcular la simulación financiera avanzada
    @PostMapping("/calcular")
    public ResponseEntity<SimulacionDTO> calcularSimulacion(@RequestBody SimulacionDTO simulacionDTO) {
        SimulacionDTO resultadoSimulacion = simulacionService.calcularSimulacion(simulacionDTO);
        return ResponseEntity.ok(resultadoSimulacion);
    }

    // Endpoint para generar el PDF de la simulación
    @PostMapping("/exportar")
    public ResponseEntity<ByteArrayResource> exportarSimulacionPdf(@RequestBody SimulacionDTO simulacionDTO) {
        // Primero, calcula la simulación para asegurar datos completos en el DTO
        SimulacionDTO resultadoSimulacion = simulacionService.calcularSimulacion(simulacionDTO);

        // Luego, genera el PDF con los datos calculados
        byte[] pdfBytes = simulacionService.generarReportePdf(resultadoSimulacion);

        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=simulacion.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(resource);
    }
}
