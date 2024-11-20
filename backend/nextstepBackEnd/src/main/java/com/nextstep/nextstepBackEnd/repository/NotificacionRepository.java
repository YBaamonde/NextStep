package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    // Obtener notificaciones de un usuario específico, ordenadas por fecha de creación (descendente)
    List<Notificacion> findByUsuarioIdOrderByFechaCreacionDesc(Integer usuarioId);

    // Contar notificaciones no leídas de un usuario
    Long countByUsuarioIdAndLeidoFalse(Integer usuarioId);
}
