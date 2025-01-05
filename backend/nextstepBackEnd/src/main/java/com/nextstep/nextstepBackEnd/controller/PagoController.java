package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.PagoDTO;
import com.nextstep.nextstepBackEnd.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // Obtener todos los pagos sin su recurrencia
    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> getPagosByUsuario(@PathVariable Integer usuarioId) {
        try {
            List<PagoDTO> pagos = pagoService.getPagosByUsuarioId(usuarioId);
            return ResponseEntity.ok(pagos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Obtener todos los pagos con su recurrencia
    @GetMapping("/recurrentes/{usuarioId}")
    public ResponseEntity<?> getPagosRecurrentesByUsuario(@PathVariable Integer usuarioId) {
        try {
            List<PagoDTO> pagosRecurrentes = pagoService.getPagosConRecurrencia(usuarioId);
            return ResponseEntity.ok(pagosRecurrentes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Crear un nuevo pago
    @PostMapping("/{usuarioId}")
    public ResponseEntity<?> createPago(@PathVariable Integer usuarioId,
                                        @RequestBody PagoDTO pagoDTO) {
        try {
            PagoDTO createdPago = pagoService.createPago(usuarioId, pagoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPago);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Actualizar un pago existente
    @PutMapping("/{pagoId}")
    public ResponseEntity<?> updatePago(@PathVariable Integer pagoId,
                                        @RequestBody PagoDTO pagoDTO) {
        try {
            PagoDTO updatedPago = pagoService.updatePago(pagoId, pagoDTO);
            return ResponseEntity.ok(updatedPago);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Eliminar un pago
    @DeleteMapping("/{pagoId}")
    public ResponseEntity<?> deletePago(@PathVariable Integer pagoId) {
        try {
            pagoService.deletePago(pagoId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
