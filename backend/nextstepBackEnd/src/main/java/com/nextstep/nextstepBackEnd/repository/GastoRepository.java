package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Gasto;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Integer> {
    // Búsqueda por entidad
    List<Gasto> findByUsuario(Usuario usuario);
    List<Gasto> findByCategoria(Categoria categoria);

    // Búsqueda directa por ID
    List<Gasto> findByUsuarioId(Integer usuarioId);
    List<Gasto> findByCategoriaId(Integer categoriaId);
}

