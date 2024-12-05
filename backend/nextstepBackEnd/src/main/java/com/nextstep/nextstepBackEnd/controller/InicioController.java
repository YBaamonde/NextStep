package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.service.GastoService;
import com.nextstep.nextstepBackEnd.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public InicioController(GastoService gastoService, PagoService pagoService) {
        this.gastoService = gastoService;
        this.pagoService = pagoService;
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
}

