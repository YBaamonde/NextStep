package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.Usuario;
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
        return notificacionConfigRepository.findByUsuarioId(usuarioId)
                .or(() -> {
                    Usuario usuario = userRepository.findById(usuarioId)
                            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
                    NotificacionConfig defaultConfig = new NotificacionConfig();
                    defaultConfig.setUsuario(usuario);
                    return Optional.of(notificacionConfigRepository.save(defaultConfig));
                });
    }




    public NotificacionConfig guardarConfiguracion(NotificacionConfig config) {
        if (config.getUsuario() == null || config.getUsuario().getId() == null) {
            throw new IllegalArgumentException("Usuario inválido en la configuración.");
        }

        Usuario usuario = userRepository.findById(config.getUsuario().getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Busca si ya existe una configuración para este usuario
        Optional<NotificacionConfig> existingConfig = notificacionConfigRepository.findByUsuarioId(usuario.getId());

        if (existingConfig.isPresent()) {
            NotificacionConfig configToUpdate = existingConfig.get();
            configToUpdate.setEmailActivas(config.isEmailActivas());
            configToUpdate.setEmailDiasAntes(config.getEmailDiasAntes());
            configToUpdate.setInAppActivas(config.isInAppActivas());
            configToUpdate.setInAppDiasAntes(config.getInAppDiasAntes());
            return notificacionConfigRepository.save(configToUpdate);
        }

        // Si no existe, crea una nueva
        config.setUsuario(usuario);
        return notificacionConfigRepository.save(config);
    }




}
