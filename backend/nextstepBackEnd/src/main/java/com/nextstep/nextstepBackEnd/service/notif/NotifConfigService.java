package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.notif.NotificacionConfig;
import com.nextstep.nextstepBackEnd.repository.NotificacionConfigRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotifConfigService {
    private final NotificacionConfigRepository notificacionConfigRepository;
    private final UserRepository userRepository;

    public NotifConfigService(NotificacionConfigRepository notificacionConfigRepository, UserRepository userRepository) {
        this.notificacionConfigRepository = notificacionConfigRepository;
        this.userRepository = userRepository;
    }

    public Optional<NotificacionConfig> obtenerConfiguracionPorUsuario(Integer usuarioId) {
        return notificacionConfigRepository.findByUsuarioId(usuarioId);
    }


    public NotificacionConfig guardarConfiguracion(NotificacionConfig config) {
        return notificacionConfigRepository.save(config);
    }
}
