package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.notif.EmailNotif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailNotifRepository extends JpaRepository<EmailNotif, Integer> {

    Optional<EmailNotif> findFirstByUsuarioIdAndPagoIdAndAsunto(Integer usuarioId, Integer pagoId, String asunto);

    boolean existsByUsuarioIdAndPagoIdAndFechaEnvio(Integer usuarioId, Integer pagoId, LocalDateTime fechaEnvio);

}
