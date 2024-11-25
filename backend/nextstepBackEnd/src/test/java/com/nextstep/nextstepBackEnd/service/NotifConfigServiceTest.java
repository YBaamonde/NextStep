package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionConfig;
import com.nextstep.nextstepBackEnd.repository.NotificacionConfigRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.service.notif.NotifConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class NotifConfigServiceTest {

    private NotificacionConfigRepository notificacionConfigRepository;
    private UserRepository userRepository;
    private NotifConfigService notifConfigService;

    @BeforeEach
    void setUp() {
        notificacionConfigRepository = Mockito.mock(NotificacionConfigRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        notifConfigService = new NotifConfigService(notificacionConfigRepository, userRepository);
    }

    @Test
    public void testObtenerConfiguracionPorUsuario_ReturnsConfig_WhenExists() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1);

        NotificacionConfig config = NotificacionConfig.builder()
                .usuario(usuario)
                .emailActivas(true)
                .emailDiasAntes(2)
                .inAppActivas(false)
                .inAppDiasAntes(1)
                .build();

        when(notificacionConfigRepository.findByUsuarioId(1)).thenReturn(Optional.of(config));

        // Act
        Optional<NotificacionConfig> result = notifConfigService.obtenerConfiguracionPorUsuario(1);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmailDiasAntes()).isEqualTo(2);
        verify(notificacionConfigRepository, times(1)).findByUsuarioId(1);
    }

    @Test
    public void testGuardarConfiguracion_SavesConfigSuccessfully() {
        // Arrange
        NotificacionConfig config = NotificacionConfig.builder()
                .emailActivas(true)
                .emailDiasAntes(3)
                .inAppActivas(false)
                .inAppDiasAntes(1)
                .build();

        when(notificacionConfigRepository.save(config)).thenReturn(config);

        // Act
        NotificacionConfig result = notifConfigService.guardarConfiguracion(config);

        // Assert
        assertThat(result).isEqualTo(config);
        verify(notificacionConfigRepository, times(1)).save(config);
    }
}