package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    // Encuentra todas las notificaciones de un usuario específico
    List<Notificacion> findByUsuarioId(Integer usuarioId);

    // Cuenta todas las notificaciones no leídas de un usuario
    long countByUsuarioIdAndLeidoFalse(Integer usuarioId);

    // Encuentra la primera notificación no leída de un usuario, pago, título y mensaje específicos
    Optional<Notificacion> findFirstByUsuarioIdAndPagoIdAndTituloAndMensajeAndLeidoFalse(
            Integer usuarioId, Integer pagoId, String titulo, String mensaje);

    // Encuentra todas las notificaciones no leídas de un usuario
    List<Notificacion> findByUsuarioIdAndLeidoFalse(Integer usuarioId);
}

