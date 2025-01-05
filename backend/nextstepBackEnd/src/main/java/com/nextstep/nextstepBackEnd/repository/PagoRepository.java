package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    // Encuentra todos los pagos de un usuario específico
    List<Pago> findByUsuarioId(Integer usuarioId);

    // Encuentra todos los pagos recurrentes de un usuario
    List<Pago> findByUsuarioIdAndRecurrenteTrue(Integer usuarioId);

    @Query("SELECT p FROM Pago p JOIN FETCH p.usuario WHERE p.fecha BETWEEN :inicio AND :fin")
    List<Pago> findPagosWithUsuarioByFechaBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

}

