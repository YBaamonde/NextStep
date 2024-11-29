package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.service.GastoService;
import com.nextstep.nextstepBackEnd.service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/inicio")
public class InicioController {
    private final GastoService gastoService;
    private final PagoService pagoService;

    public InicioController(GastoService gastoService, PagoService pagoService) {
        this.gastoService = gastoService;
        this.pagoService = pagoService;
    }

    // Obtener datos para la página de inicio
    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> getInicioData(@PathVariable Integer usuarioId) {
        Map<String, Object> response = new HashMap<>();
        response.put("pagosProximos", pagoService.getPagosByUsuarioId(usuarioId));
        response.put("gastosPorCategoria", gastoService.getGastosPorCategoria(usuarioId));
        response.put("evolucionTrimestral", gastoService.getGastosPorTrimestre(usuarioId));
        return ResponseEntity.ok(response);
    }

}