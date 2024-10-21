package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // Endpoint para obtener todas las categorías de un usuario
    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias(@RequestParam("usuarioId") Integer usuarioId) {
        List<Categoria> categorias = categoriaService.findAllByUsuarioId(usuarioId);
        return ResponseEntity.ok(categorias);
    }

    // Endpoint para crear una nueva categoría
    @PostMapping
    public ResponseEntity<?> createCategoria(@RequestBody Categoria categoria) {
        if (categoriaService.countByUsuarioId(categoria.getUsuario().getId()) >= 15) {
            return ResponseEntity.badRequest().body("No se pueden crear más de 15 categorías.");
        }

        Categoria newCategoria = categoriaService.createCategoria(categoria);
        return ResponseEntity.ok(newCategoria);
    }

    // Endpoint para actualizar una categoría existente
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Integer id, @RequestBody Categoria categoria) {
        Categoria updatedCategoria = categoriaService.updateCategoria(id, categoria);
        return ResponseEntity.ok(updatedCategoria);
    }

    // Endpoint para eliminar una categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable Integer id) {
        if (categoriaService.countAll() <= 1) {
            return ResponseEntity.badRequest().body("Debe haber al menos una categoría.");
        }

        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
