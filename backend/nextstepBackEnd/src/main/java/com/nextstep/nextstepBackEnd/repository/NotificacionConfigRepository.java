package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.notif.NotificacionConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificacionConfigRepository extends JpaRepository<NotificacionConfig, Integer> {

    Optional<NotificacionConfig> findByUsuarioId(Integer usuarioId);

}
