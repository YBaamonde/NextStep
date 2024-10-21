package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.Gasto;
import com.nextstep.nextstepBackEnd.service.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;

    // Endpoint para obtener todos los gastos de un usuario
    @GetMapping
    public ResponseEntity<List<Gasto>> getAllGastos(@RequestParam("usuarioId") Integer usuarioId) {
        List<Gasto> gastos = gastoService.findAllByUsuarioId(usuarioId);
        return ResponseEntity.ok(gastos);
    }

    // Endpoint para crear un nuevo gasto
    @PostMapping
    public ResponseEntity<Gasto> createGasto(@RequestBody Gasto gasto) {
        Gasto newGasto = gastoService.createGasto(gasto);
        return ResponseEntity.ok(newGasto);
    }

    // Endpoint para actualizar un gasto existente
    @PutMapping("/{id}")
    public ResponseEntity<Gasto> updateGasto(@PathVariable Integer id, @RequestBody Gasto gasto) {
        Gasto updatedGasto = gastoService.updateGasto(id, gasto);
        return ResponseEntity.ok(updatedGasto);
    }

    // Endpoint para eliminar un gasto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGasto(@PathVariable Integer id) {
        gastoService.deleteGasto(id);
        return ResponseEntity.noContent().build();
    }
}
