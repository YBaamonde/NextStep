package com.nextstep.nextstepBackEnd.controller;

import com.nextstep.nextstepBackEnd.controller.notif.NotifConfigController;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionConfig;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.service.notif.NotifConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class NotifConfigControllerTest {

    private NotifConfigService notifConfigService;
    private NotifConfigController notifConfigController;

    @BeforeEach
    void setUp() {
        notifConfigService = Mockito.mock(NotifConfigService.class);
        notifConfigController = new NotifConfigController(notifConfigService);
    }

    @Test
    public void testObtenerConfiguracion_ReturnsConfig_WhenExists() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1);

        NotificacionConfig config = NotificacionConfig.builder()
                .usuario(usuario)
                .emailActivas(true)
                .emailDiasAntes(3)
                .inAppActivas(false)
                .inAppDiasAntes(1)
                .build();

        when(notifConfigService.obtenerConfiguracionPorUsuario(1)).thenReturn(Optional.of(config));

        // Act
        ResponseEntity<NotificacionConfig> response = notifConfigController.obtenerConfiguracion(1);

        // Assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(config);
        verify(notifConfigService, times(1)).obtenerConfiguracionPorUsuario(1);
    }

    @Test
    public void testGuardarConfiguracion_SavesConfigSuccessfully() {
        // Arrange
        NotificacionConfig config = NotificacionConfig.builder()
                .emailActivas(true)
                .emailDiasAntes(2)
                .inAppActivas(false)
                .inAppDiasAntes(1)
                .build();

        when(notifConfigService.guardarConfiguracion(config)).thenReturn(config);

        // Act
        ResponseEntity<NotificacionConfig> response = notifConfigController.guardarConfiguracion(config);

        // Assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(config);
        verify(notifConfigService, times(1)).guardarConfiguracion(config);
    }
}
