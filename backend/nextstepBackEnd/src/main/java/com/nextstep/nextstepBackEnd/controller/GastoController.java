package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.Gasto;
import com.nextstep.nextstepBackEnd.service.GastoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gastos")
public class GastoController {

    private final GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @GetMapping("/{usuarioId}")
    public List<Gasto> getGastosByUsuario(@PathVariable Integer usuarioId) {
        return gastoService.getGastosByUsuarioId(usuarioId);
    }

    @PostMapping("/{usuarioId}/{categoriaId}")
    public Gasto createGasto(@PathVariable Integer usuarioId, @PathVariable Integer categoriaId, @RequestBody Gasto gasto) {
        return gastoService.createGasto(usuarioId, categoriaId, gasto);
    }

    @PutMapping("/{gastoId}")
    public Gasto updateGasto(@PathVariable Integer gastoId, @RequestBody Gasto gasto) {
        return gastoService.updateGasto(gastoId, gasto);
    }

    @DeleteMapping("/{gastoId}")
    public ResponseEntity<Void> deleteGasto(@PathVariable Integer gastoId) {
        gastoService.deleteGasto(gastoId);
        return ResponseEntity.ok().build();
    }
}
