package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionConfig;
import com.nextstep.nextstepBackEnd.repository.NotificacionConfigRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.service.notif.NotifConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotifConfigServiceTest {



    @InjectMocks
    private NotifConfigService notifConfigService;

    @Mock
    private NotificacionConfigRepository notificacionConfigRepository;

    @Mock
    private UserRepository userRepository;

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
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("testuser");

        // Simula que el usuario existe en el repositorio
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));

        NotificacionConfig newConfig = NotificacionConfig.builder()
                .usuario(usuario) // Inicializa la relación obligatoria
                .emailActivas(true)
                .emailDiasAntes(3)
                .inAppActivas(false)
                .inAppDiasAntes(1)
                .build();

        // Simula que no existe una configuración previa
        when(notificacionConfigRepository.findByUsuarioId(1)).thenReturn(Optional.empty());

        // Simula guardar la nueva configuración
        when(notificacionConfigRepository.save(any(NotificacionConfig.class))).thenReturn(newConfig);

        // Act
        NotificacionConfig result = notifConfigService.guardarConfiguracion(newConfig);

        // Assert
        assertThat(result).isEqualTo(newConfig); // Verifica que el resultado es el esperado
        verify(userRepository, times(1)).findById(1); // Verifica que se buscó el usuario
        verify(notificacionConfigRepository, times(1)).findByUsuarioId(1); // Verifica que se buscó la configuración existente
        verify(notificacionConfigRepository, times(1)).save(any(NotificacionConfig.class)); // Verifica que se guardó la nueva configuración
    }

    @Test
    public void testObtenerConfiguracionPorUsuario_CreatesDefaultConfigWhenNotFound() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("testuser");

        NotificacionConfig defaultConfig = new NotificacionConfig();
        defaultConfig.setUsuario(usuario);

        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(notificacionConfigRepository.findByUsuarioId(1)).thenReturn(Optional.empty());
        when(notificacionConfigRepository.save(any(NotificacionConfig.class))).thenReturn(defaultConfig);

        // Act
        Optional<NotificacionConfig> result = notifConfigService.obtenerConfiguracionPorUsuario(1);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUsuario()).isEqualTo(usuario); // Verifica que se creó con el usuario correcto
        verify(userRepository, times(1)).findById(1); // Verifica la búsqueda del usuario
        verify(notificacionConfigRepository, times(1)).findByUsuarioId(1); // Verifica la búsqueda inicial de la configuración
        verify(notificacionConfigRepository, times(1)).save(any(NotificacionConfig.class)); // Verifica que se guardó la configuración predeterminada
    }


}