package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.notif.EmailNotif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailNotifRepository extends JpaRepository<EmailNotif, Integer> {

    Optional<EmailNotif> findFirstByUsuarioIdAndPagoIdAndAsunto(Integer usuarioId, Integer pagoId, String asunto);

    boolean existsByUsuarioIdAndPagoId(Integer usuarioId, Integer pagoId);
}
