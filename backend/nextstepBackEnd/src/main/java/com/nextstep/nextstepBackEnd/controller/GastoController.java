package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.Gasto;
import com.nextstep.nextstepBackEnd.model.GastoDTO;
import com.nextstep.nextstepBackEnd.service.GastoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gastos")
public class GastoController {

    private final GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @GetMapping("/{usuarioId}")
    public List<GastoDTO> getGastosByUsuario(@PathVariable Integer usuarioId) {
        return gastoService.getGastosByUsuarioId(usuarioId)
                .stream()
                .map(gasto -> new GastoDTO(
                        gasto.getId(),
                        gasto.getNombre(),
                        gasto.getMonto(),
                        gasto.getFecha(),
                        gasto.getCategoriaId()))
                .collect(Collectors.toList());
    }

    @PostMapping("/{usuarioId}/{categoriaId}")
    public ResponseEntity<GastoDTO> createGasto(@PathVariable Integer usuarioId,
                                                @PathVariable Integer categoriaId,
                                                @RequestBody GastoDTO gastoDTO) {
        GastoDTO createdGasto = gastoService.createGasto(usuarioId, categoriaId, gastoDTO);
        return ResponseEntity.ok(createdGasto);
    }

    @PutMapping("/{gastoId}")
    public ResponseEntity<GastoDTO> updateGasto(@PathVariable Integer gastoId,
                                                @RequestBody GastoDTO gastoDTO) {
        GastoDTO updatedGasto = gastoService.updateGasto(gastoId, gastoDTO);
        return ResponseEntity.ok(updatedGasto);
    }

    @DeleteMapping("/{gastoId}")
    public ResponseEntity<Void> deleteGasto(@PathVariable Integer gastoId) {
        gastoService.deleteGasto(gastoId);
        return ResponseEntity.ok().build();
    }

    // Obtener gastos históricos agrupados por categoría
    @GetMapping("/categoria/{categoriaId}/historicos")
    public ResponseEntity<List<GastoDTO>> getGastosHistoricosPorCategoria(@PathVariable Integer categoriaId) {
        List<GastoDTO> gastos = gastoService.getGastosByCategoriaConLimite(categoriaId, Integer.MAX_VALUE); // Sin límite
        return ResponseEntity.ok(gastos);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<GastoDTO>> getGastosPorCategoriaConLimite(
            @PathVariable Integer categoriaId,
            @RequestParam(defaultValue = "10") int limite) {
        List<GastoDTO> gastos = gastoService.getGastosByCategoriaConLimite(categoriaId, limite);
        return ResponseEntity.ok(gastos);
    }


}
