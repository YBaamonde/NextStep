package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public Categoria createCategoria(String nombre, String descripcion) {
        if (categoriaRepository.findByNombre(nombre).isPresent()) {
            throw new RuntimeException("La categoría ya existe");
        }
        Categoria categoria = Categoria.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .build();
        return categoriaRepository.save(categoria);
    }

    public void deleteCategoria(Integer categoriaId) {
        categoriaRepository.deleteById(categoriaId);
    }

    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }
}

