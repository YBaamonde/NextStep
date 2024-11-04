package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Gasto;
import com.nextstep.nextstepBackEnd.model.GastoDTO;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.CategoriaRepository;
import com.nextstep.nextstepBackEnd.repository.GastoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GastoService {

    private final GastoRepository gastoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UserRepository userRepository;

    public GastoService(GastoRepository gastoRepository, CategoriaRepository categoriaRepository, UserRepository userRepository) {
        this.gastoRepository = gastoRepository;
        this.categoriaRepository = categoriaRepository;
        this.userRepository = userRepository;
    }

    // Obtener todos los gastos de un usuario por ID (usando búsqueda directa)
    public List<Gasto> getGastosByUsuarioId(Integer usuarioId) {
        return gastoRepository.findByUsuarioId(usuarioId);
    }

    // Crear un nuevo gasto usando GastoDTO
    public Gasto createGasto(Integer usuarioId, Integer categoriaId, GastoDTO gastoDTO) {
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada."));

        Gasto newGasto = new Gasto();
        newGasto.setNombre(gastoDTO.getNombre());
        newGasto.setMonto(gastoDTO.getMonto());
        newGasto.setFecha(gastoDTO.getFecha());
        newGasto.setUsuario(usuario);
        newGasto.setCategoria(categoria);

        return gastoRepository.save(newGasto);
    }

    // Actualizar un gasto existente usando GastoDTO
    public Gasto updateGasto(Integer gastoId, GastoDTO gastoDTO) {
        if (gastoId == null) {
            throw new IllegalArgumentException("El ID del gasto no debe ser null");
        }
        return gastoRepository.findById(gastoId).map(existingGasto -> {
            existingGasto.setNombre(gastoDTO.getNombre());
            existingGasto.setMonto(gastoDTO.getMonto());
            existingGasto.setFecha(gastoDTO.getFecha());
            return gastoRepository.save(existingGasto);
        }).orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado."));
    }


    // Eliminar un gasto
    public void deleteGasto(Integer gastoId) {
        if (gastoRepository.existsById(gastoId)) {
            gastoRepository.deleteById(gastoId);
        } else {
            throw new IllegalArgumentException("Gasto no encontrado.");
        }
    }
}
