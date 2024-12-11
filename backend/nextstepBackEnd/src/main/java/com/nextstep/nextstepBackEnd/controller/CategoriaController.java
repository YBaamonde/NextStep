package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/{usuarioId}")
    public List<Categoria> getCategoriasByUsuario(@PathVariable Integer usuarioId) {
        return categoriaService.getCategoriasByUsuarioId(usuarioId);
    }

    @PostMapping("/{usuarioId}")
    public Categoria createCategoria(@PathVariable Integer usuarioId, @RequestBody Categoria categoria) {
        return categoriaService.createCategoria(usuarioId, categoria);
    }

    @PutMapping("/{categoriaId}")
    public Categoria updateCategoria(@PathVariable Integer categoriaId, @RequestBody Categoria categoria) {
        //System.out.println("Actualización de categoría solicitada para ID: " + categoriaId); // Depuración
        logger.info("Actualización de categoría solicitada para ID: " + categoriaId);

        // Confirmar los detalles de la categoría antes de actualizar
        //System.out.println("Detalles de la categoría para actualizar: Nombre: " + categoria.getNombre() + ", Descripción: " + categoria.getDescripcion());
        logger.info("Detalles de la categoría para actualizar: Nombre: " + categoria.getNombre() + ", Descripción: " + categoria.getDescripcion());

        return categoriaService.updateCategoria(categoriaId, categoria);
    }


    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer categoriaId) {
        categoriaService.deleteCategoria(categoriaId);
        return ResponseEntity.ok().build();
    }
}
