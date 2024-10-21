package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Gasto;
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

    // Metodo para obtener todos los gastos de un usuario
    public List<Gasto> getGastosByUsuarioId(Integer usuarioId) {
        Optional<Usuario> usuario = userRepository.findById(usuarioId);
        return usuario.map(gastoRepository::findByUsuario).orElse(Collections.emptyList());
    }

    // Metodo para crear un gasto
    public Gasto createGasto(Integer usuarioId, Integer categoriaId, Gasto gasto) {
        Optional<Usuario> usuario = userRepository.findById(usuarioId);
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);

        if (usuario.isPresent() && categoria.isPresent()) {
            gasto.setUsuario(usuario.get());
            gasto.setCategoria(categoria.get());
            return gastoRepository.save(gasto);
        } else {
            throw new IllegalArgumentException("Usuario o categoría no encontrados.");
        }
    }

    // Metodo para actualizar un gasto existente
    public Gasto updateGasto(Integer gastoId, Gasto updatedGasto) {
        return gastoRepository.findById(gastoId).map(existingGasto -> {
            existingGasto.setNombre(updatedGasto.getNombre());
            existingGasto.setMonto(updatedGasto.getMonto());
            existingGasto.setFecha(updatedGasto.getFecha());
            return gastoRepository.save(existingGasto);
        }).orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado."));
    }

    // Metodo para eliminar un gasto por ID
    public void deleteGasto(Integer gastoId) {
        if (gastoRepository.existsById(gastoId)) {
            gastoRepository.deleteById(gastoId);
        } else {
            throw new IllegalArgumentException("Gasto no encontrado.");
        }
    }
}
