package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.PagoDTO;
import com.nextstep.nextstepBackEnd.service.PagoService;
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

    // Obtener todos los pagos de un usuario
    @GetMapping("/{usuarioId}")
    public List<PagoDTO> getPagosByUsuario(@PathVariable Integer usuarioId) {
        return pagoService.getPagosByUsuarioId(usuarioId)
                .stream()
                .map(pago -> new PagoDTO(
                        pago.getId(),
                        pago.getNombre(),
                        pago.getMonto(),
                        pago.getFecha(),
                        pago.getRecurrente(),
                        pago.getFrecuencia()))
                .collect(Collectors.toList());
    }

    // Crear un nuevo pago
    @PostMapping("/{usuarioId}")
    public ResponseEntity<PagoDTO> createPago(@PathVariable Integer usuarioId,
                                              @RequestBody PagoDTO pagoDTO) {
        PagoDTO createdPago = pagoService.createPago(usuarioId, pagoDTO);
        return ResponseEntity.ok(createdPago);
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
        return ResponseEntity.ok().build();
    }

    // Obtener todos los pagos recurrentes de un usuario
    @GetMapping("/recurrentes/{usuarioId}")
    public List<PagoDTO> getPagosRecurrentesByUsuario(@PathVariable Integer usuarioId) {
        return pagoService.getPagosRecurrentesByUsuarioId(usuarioId)
                .stream()
                .map(pago -> new PagoDTO(
                        pago.getId(),
                        pago.getNombre(),
                        pago.getMonto(),
                        pago.getFecha(),
                        pago.getRecurrente(),
                        pago.getFrecuencia()))
                .collect(Collectors.toList());
    }
}

