package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.notif.EmailNotif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailNotifRepository extends JpaRepository<EmailNotif, Integer> {

    Optional<EmailNotif> findFirstByUsuarioIdAndPagoIdAndAsunto(Integer usuarioId, Integer pagoId, String asunto);

    //boolean existsByUsuarioIdAndPagoIdAndFechaEnvio(Integer usuarioId, Integer pagoId, LocalDateTime fechaEnvio);

    @Query("SELECT COUNT(e) > 0 FROM EmailNotif e WHERE e.usuario.id = :usuarioId AND e.pago.id = :pagoId AND DATE(e.fechaEnvio) = DATE(:fechaEnvio)")
    boolean existsByUsuarioIdAndPagoIdAndFechaEnvio(@Param("usuarioId") Integer usuarioId,
                                                    @Param("pagoId") Integer pagoId,
                                                    @Param("fechaEnvio") LocalDateTime fechaEnvio);


}
