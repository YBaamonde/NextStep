package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.service.GastoService;
import com.nextstep.nextstepBackEnd.service.PagoService;
import com.nextstep.nextstepBackEnd.service.pdf.InformePdfService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/inicio")
public class InicioController {

    private final GastoService gastoService;
    private final PagoService pagoService;
    private final InformePdfService informePdfService;

    public InicioController(GastoService gastoService, PagoService pagoService, InformePdfService informePdfService) {
        this.gastoService = gastoService;
        this.pagoService = pagoService;
        this.informePdfService = informePdfService;
    }

    // Obtener datos para la página de inicio
    @GetMapping("/{usuarioId}")
    public ResponseEntity<Map<String, Object>> getInicioData(@PathVariable Integer usuarioId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Agregar pagos próximos (incluyendo recurrencias)
            response.put("pagosProximos", pagoService.getPagosConRecurrencia(usuarioId));

            // Agregar gastos por categoría
            response.put("gastosPorCategoria", gastoService.getGastosPorCategoria(usuarioId));

            // Agregar evolución trimestral de gastos
            response.put("evolucionTrimestral", gastoService.getGastosPorTrimestre(usuarioId));

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error inesperado: " + e.getMessage()));
        }
    }


    // Generar PDF de evolución trimestral de gastos
    @GetMapping("/informe/{usuarioId}")
    public ResponseEntity<byte[]> generarInformeEvolucionTrimestral(@PathVariable Integer usuarioId) {
        try {
            Map<String, Double> evolucionTrimestral = gastoService.getGastosPorTrimestre(usuarioId);

            byte[] pdfContent = informePdfService.generarPdfEvolucionTrimestral(evolucionTrimestral);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("Informe_Evolucion_Trimestral.pdf")
                    .build());

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage().getBytes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar el informe".getBytes());
        }
    }
}

