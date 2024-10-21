package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Optional<Categoria> findByNombre(String nombre);
    List<Categoria> findByUsuario(Usuario usuario);
}
