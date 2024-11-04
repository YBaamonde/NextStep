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

    // Obtener todos los gastos de un usuario
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

    // Crear un nuevo gasto
    @PostMapping("/{usuarioId}/{categoriaId}")
    public ResponseEntity<GastoDTO> createGasto(@PathVariable Integer usuarioId,
                                                @PathVariable Integer categoriaId,
                                                @RequestBody GastoDTO gastoDTO) {
        Gasto createdGasto = gastoService.createGasto(usuarioId, categoriaId, gastoDTO);
        GastoDTO responseDTO = new GastoDTO(
                createdGasto.getId(),
                createdGasto.getNombre(),
                createdGasto.getMonto(),
                createdGasto.getFecha(),
                createdGasto.getCategoria().getId()
        );
        return ResponseEntity.ok(responseDTO);
    }

    // Actualizar un gasto existente
    @PutMapping("/{gastoId}")
    public ResponseEntity<GastoDTO> updateGasto(@PathVariable Integer gastoId, @RequestBody GastoDTO gastoDTO) {
        // Verifica que gastoId no sea null
        if (gastoId == null) {
            throw new IllegalArgumentException("El ID del gasto no debe ser null");
        }
        Gasto updatedGasto = gastoService.updateGasto(gastoId, gastoDTO);
        GastoDTO responseDTO = new GastoDTO(
                updatedGasto.getId(),
                updatedGasto.getNombre(),
                updatedGasto.getMonto(),
                updatedGasto.getFecha(),
                updatedGasto.getCategoria().getId()
        );
        return ResponseEntity.ok(responseDTO);
    }


    // Eliminar un gasto
    @DeleteMapping("/{gastoId}")
    public ResponseEntity<Void> deleteGasto(@PathVariable Integer gastoId) {
        gastoService.deleteGasto(gastoId);
        return ResponseEntity.ok().build();
    }
}
