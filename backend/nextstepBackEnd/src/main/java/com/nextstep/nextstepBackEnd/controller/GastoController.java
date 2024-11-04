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
    private GastoDTO gastoDTO;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @GetMapping("/{usuarioId}")
    public List<GastoDTO> getGastosByUsuario(@PathVariable Integer usuarioId) {
        List<Gasto> gastos = gastoService.getGastosByUsuarioId(usuarioId);
        return gastos.stream()
                .map(gasto -> new GastoDTO(
                        gasto.getId(),
                        gasto.getNombre(),
                        gasto.getMonto(),
                        gasto.getFecha(),
                        gasto.getCategoria().getId() // Incluye el ID de la categoría
                ))
                .collect(Collectors.toList());
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
