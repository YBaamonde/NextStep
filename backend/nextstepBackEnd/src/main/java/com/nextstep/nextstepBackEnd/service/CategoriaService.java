package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.CategoriaRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UserRepository userRepository;

    // Obtener todas las categorías de un usuario específico
    public List<Categoria> getCategoriasByUsuarioId(Integer usuarioId) {
        Optional<Usuario> usuario = userRepository.findById(usuarioId);
        return usuario.map(categoriaRepository::findByUsuario).orElse(Collections.emptyList());
    }

    // Crear una nueva categoría para un usuario
    public Categoria createCategoria(Integer usuarioId, Categoria categoria) {
        Optional<Usuario> usuario = userRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            categoria.setUsuario(usuario.get());
            return categoriaRepository.save(categoria);
        } else {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }
    }

    // Actualizar una categoría existente
    public Categoria updateCategoria(Integer categoriaId, Categoria categoriaDetails) {
        return categoriaRepository.findById(categoriaId).map(categoria -> {
            categoria.setNombre(categoriaDetails.getNombre());
            categoria.setDescripcion(categoriaDetails.getDescripcion());
            return categoriaRepository.save(categoria);
        }).orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada."));
    }

    // Metodo para eliminar una categoría
    public boolean deleteCategoria(Integer categoriaId) {
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
        if (categoria.isPresent()) {
            categoriaRepository.deleteById(categoriaId);
            return true;  // Retorna true si la categoría fue eliminada con éxito
        }
        return false;  // Retorna false si no se encontró la categoría
    }
}
