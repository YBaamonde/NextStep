package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByUsuarioId(Integer usuarioId);

    long countByUsuarioIdAndLeidoFalse(Integer usuarioId);
}

