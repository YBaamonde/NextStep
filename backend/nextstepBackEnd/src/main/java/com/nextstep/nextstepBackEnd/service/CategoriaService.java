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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);

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
        //System.out.println("Buscando categoría para actualizar con ID: " + categoriaId); // Depuración
        logger.info("Buscando categoría para actualizar con ID: " + categoriaId);

        return categoriaRepository.findById(categoriaId).map(categoria -> {
            //System.out.println("Categoría encontrada, actualizando nombre y descripción"); // Confirmar que encontró la categoría
            logger.info("Categoría encontrada, actualizando nombre y descripción");

            // Mostrar el cambio antes de guardar (Debug)
            //System.out.println("Nuevo Nombre: " + categoriaDetails.getNombre());
            //System.out.println("Nueva Descripción: " + categoriaDetails.getDescripcion());
            logger.info("Nuevo Nombre: " + categoriaDetails.getNombre());
            logger.info("Nueva Descripción: " + categoriaDetails.getDescripcion());

            categoria.setNombre(categoriaDetails.getNombre());
            categoria.setDescripcion(categoriaDetails.getDescripcion());
            return categoriaRepository.save(categoria);
        }).orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada."));
    }


    // Metodo para eliminar una categoría
    public void deleteCategoria(Integer categoriaId) {
        if (categoriaRepository.existsById(categoriaId)) {
            categoriaRepository.deleteById(categoriaId);
        } else {
            throw new IllegalArgumentException("Categoría no encontrada.");
        }
    }

}
