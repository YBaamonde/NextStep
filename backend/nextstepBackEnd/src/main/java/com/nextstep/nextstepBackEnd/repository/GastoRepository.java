package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Gasto;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Integer> {
    // Búsqueda por entidad
    List<Gasto> findByUsuario(Usuario usuario);
    List<Gasto> findByCategoria(Categoria categoria);

    // Búsqueda directa por ID
    List<Gasto> findByUsuarioId(Integer usuarioId);
    List<Gasto> findByCategoriaId(Integer categoriaId);

    // Búsqueda de gastos agrupados por categoría
    @Query("SELECT g.categoria.nombre AS categoria, SUM(g.monto) AS total " +
            "FROM Gasto g WHERE g.usuario.id = :usuarioId GROUP BY g.categoria.nombre")
    List<Object[]> findGastosGroupedByCategoria(@Param("usuarioId") Integer usuarioId);

    // Limitar la búsqueda a un número de resultados
    @Query("SELECT g FROM Gasto g WHERE g.categoria.id = :categoriaId ORDER BY g.fecha DESC")
    Page<Gasto> findTopGastosByCategoria(@Param("categoriaId") Integer categoriaId, Pageable pageable); // Pageable limita la búsqueda a un número de resultados

}

