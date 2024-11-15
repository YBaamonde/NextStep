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
        // Generar el PDF como un array de bytes en memoria
        byte[] pdfBytes = simulacionPdfService.generarPdfSimulacion(simulacionDTO);

        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("El PDF no se generó correctamente");
        }

        // Enviar el PDF directamente en la respuesta HTTP
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=simulacion.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(resource);
    }

}
