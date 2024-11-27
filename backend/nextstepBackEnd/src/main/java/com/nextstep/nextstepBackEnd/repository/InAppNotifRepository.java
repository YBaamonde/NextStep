package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.notif.InAppNotif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InAppNotifRepository extends JpaRepository<InAppNotif, Integer> {

    // Encuentra todas las notificaciones de un usuario específico
    List<InAppNotif> findByUsuarioId(Integer usuarioId);

    // Cuenta todas las notificaciones no leídas de un usuario
    long countByUsuarioIdAndLeidoFalse(Integer usuarioId);

    // Encuentra todas las notificaciones no leídas de un usuario
    List<InAppNotif> findByUsuarioIdAndLeidoFalse(Integer usuarioId);

    Optional<InAppNotif> findFirstByUsuarioIdAndPagoIdAndTituloAndMensajeAndFechaCreacion(
            Integer usuarioId, Integer pagoId, String titulo, String mensaje, LocalDateTime fechaCreacion);

}

