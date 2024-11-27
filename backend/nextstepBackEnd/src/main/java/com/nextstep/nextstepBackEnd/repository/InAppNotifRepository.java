package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.notif.InAppNotif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InAppNotifRepository extends JpaRepository<InAppNotif, Integer> {

    // Encuentra todas las notificaciones de un usuario específico
    List<InAppNotif> findByUsuarioId(Integer usuarioId);

    // Cuenta todas las notificaciones no leídas de un usuario
    long countByUsuarioIdAndLeidoFalse(Integer usuarioId);

    // Encuentra la primera notificación no leída de un usuario, pago, título y mensaje específicos
    Optional<InAppNotif> findFirstByUsuarioIdAndPagoIdAndTituloAndMensajeAndLeidoFalse(
            Integer usuarioId, Integer pagoId, String titulo, String mensaje);

    // Encuentra todas las notificaciones no leídas de un usuario
    List<InAppNotif> findByUsuarioIdAndLeidoFalse(Integer usuarioId);
}

