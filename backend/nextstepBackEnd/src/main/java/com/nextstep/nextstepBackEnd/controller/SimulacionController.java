package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import com.nextstep.nextstepBackEnd.service.SimulacionPdfService;
import com.nextstep.nextstepBackEnd.service.SimulacionService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private final SimulacionPdfService simulacionPdfService;

    public SimulacionController(SimulacionService simulacionService, SimulacionPdfService simulacionPdfService) {
        this.simulacionService = simulacionService;
        this.simulacionPdfService = simulacionPdfService;
    }

    // Endpoint para calcular la simulación financiera avanzada
    @PostMapping("/calcular")
    public ResponseEntity<SimulacionDTO> calcularSimulacion(@RequestBody SimulacionDTO simulacionDTO) {
        // Debug: Verificar los datos recibidos en el backend
        System.out.println("Datos recibidos en el backend para simulación: " + simulacionDTO); // Debug

        // Procesar la simulación
        SimulacionDTO resultadoSimulacion = simulacionService.calcularSimulacion(simulacionDTO);

        // Debug: Verificar los resultados antes de enviarlos de vuelta
        System.out.println("Resultado de la simulación: " + resultadoSimulacion); // Debug

        // Devolver la respuesta como JSON
        return ResponseEntity.ok(resultadoSimulacion);
    }

    // Endpoint para generar el PDF de la simulación
    @PostMapping("/exportar")
    public ResponseEntity<ByteArrayResource> exportarSimulacionPdf(@RequestBody SimulacionDTO simulacionDTO) {
        if (simulacionDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        byte[] pdfContent = simulacionPdfService.generarPdfSimulacion(simulacionDTO);
        if (pdfContent == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        ByteArrayResource resource = new ByteArrayResource(pdfContent);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=simulacion.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfContent.length)
                .body(resource);
    }


}
