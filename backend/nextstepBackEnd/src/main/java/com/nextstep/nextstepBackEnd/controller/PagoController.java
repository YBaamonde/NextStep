package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.PagoDTO;
import com.nextstep.nextstepBackEnd.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // Obtener todos los pagos sin su recurrencia
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<PagoDTO>> getPagosByUsuario(@PathVariable Integer usuarioId) {
        List<PagoDTO> pagosConRecurrencia = pagoService.getPagosByUsuarioId(usuarioId);
        return ResponseEntity.ok(pagosConRecurrencia);
    }

    // Obtener todos los pagos con su recurrencia
    @GetMapping("/recurrentes/{usuarioId}")
    public ResponseEntity<List<PagoDTO>> getPagosRecurrentesByUsuario(@PathVariable Integer usuarioId) {
        List<PagoDTO> pagosRecurrentes = pagoService.getPagosConRecurrencia(usuarioId);
        return ResponseEntity.ok(pagosRecurrentes);
    }

    // Crear un nuevo pago
    @PostMapping("/{usuarioId}")
    public ResponseEntity<PagoDTO> createPago(@PathVariable Integer usuarioId,
                                              @RequestBody PagoDTO pagoDTO) {
        PagoDTO createdPago = pagoService.createPago(usuarioId, pagoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPago);
    }

    // Actualizar un pago existente
    @PutMapping("/{pagoId}")
    public ResponseEntity<PagoDTO> updatePago(@PathVariable Integer pagoId,
                                              @RequestBody PagoDTO pagoDTO) {
        PagoDTO updatedPago = pagoService.updatePago(pagoId, pagoDTO);
        return ResponseEntity.ok(updatedPago);
    }

    // Eliminar un pago
    @DeleteMapping("/{pagoId}")
    public ResponseEntity<Void> deletePago(@PathVariable Integer pagoId) {
        pagoService.deletePago(pagoId);
        return ResponseEntity.noContent().build();
    }

    /*
    // Obtener todos los pagos recurrentes de un usuario
    @GetMapping("/recurrentes/{usuarioId}")
    public ResponseEntity<List<PagoDTO>> getPagosRecurrentesByUsuario(@PathVariable Integer usuarioId) {
        List<PagoDTO> pagosRecurrentes = pagoService.getPagosRecurrentesByUsuarioId(usuarioId);
        return ResponseEntity.ok(pagosRecurrentes);
    }
     */
}
