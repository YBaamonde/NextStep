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
import java.util.stream.Collectors;

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
    public List<GastoDTO> getGastosByUsuarioId(Integer usuarioId) {
        Optional<Usuario> usuarioOpt = userRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        List<Gasto> gastos = gastoRepository.findByUsuario(usuario);

        // Convertir cada Gasto a GastoDTO
        return gastos.stream()
                .map(gasto -> new GastoDTO(
                        gasto.getId(),
                        gasto.getNombre(),
                        gasto.getMonto(),
                        gasto.getFecha(),
                        gasto.getCategoria().getId()
                ))
                .collect(Collectors.toList());
    }


    // Crear un nuevo gasto usando GastoDTO
    public GastoDTO createGasto(Integer usuarioId, Integer categoriaId, GastoDTO gastoDTO) {
        Optional<Usuario> usuario = userRepository.findById(usuarioId);
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);

        if (usuario.isPresent() && categoria.isPresent()) {
            Gasto gasto = new Gasto();
            gasto.setNombre(gastoDTO.getNombre());
            gasto.setMonto(gastoDTO.getMonto());
            gasto.setFecha(gastoDTO.getFecha());
            gasto.setUsuario(usuario.get());
            gasto.setCategoria(categoria.get());
            Gasto savedGasto = gastoRepository.save(gasto);
            return new GastoDTO(savedGasto.getId(), savedGasto.getNombre(), savedGasto.getMonto(), savedGasto.getFecha(), savedGasto.getCategoria().getId());
        } else {
            throw new IllegalArgumentException("Usuario o categoría no encontrados.");
        }
    }

    public GastoDTO updateGasto(Integer gastoId, GastoDTO gastoDTO) {
        return gastoRepository.findById(gastoId).map(existingGasto -> {
            existingGasto.setNombre(gastoDTO.getNombre());
            existingGasto.setMonto(gastoDTO.getMonto());
            existingGasto.setFecha(gastoDTO.getFecha());
            Gasto updatedGasto = gastoRepository.save(existingGasto);
            return new GastoDTO(updatedGasto.getId(), updatedGasto.getNombre(), updatedGasto.getMonto(), updatedGasto.getFecha(), updatedGasto.getCategoria().getId());
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
