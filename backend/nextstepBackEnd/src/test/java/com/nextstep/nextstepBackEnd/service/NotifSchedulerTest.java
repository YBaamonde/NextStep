package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionConfig;
import com.nextstep.nextstepBackEnd.repository.EmailNotifRepository;
import com.nextstep.nextstepBackEnd.repository.NotificacionConfigRepository;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import com.nextstep.nextstepBackEnd.service.notif.EmailNotifService;
import com.nextstep.nextstepBackEnd.service.notif.InAppNotifService;
import com.nextstep.nextstepBackEnd.service.notif.NotifScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotifSchedulerTest {

    @InjectMocks
    private NotifScheduler notifScheduler;

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private EmailNotifService emailNotifService;

    @Mock
    private InAppNotifService inAppNotifService;

    @Mock
    private NotificacionConfigRepository notificacionConfigRepository;

    @Mock
    private EmailNotifRepository emailNotifRepository;

    private Usuario usuario;
    private Pago pago;
    private NotificacionConfig config;

    @BeforeEach
    void setUp() {
        // Mock del usuario
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("testuser");
        usuario.setEmail("testuser@example.com");

        // Mock del pago
        pago = new Pago();
        pago.setId(1);
        pago.setUsuario(usuario);
        pago.setNombre("Pago Mensual");
        pago.setMonto(BigDecimal.valueOf(100));
        pago.setFecha(LocalDate.now().plusDays(3));

        // Mock de la configuración de notificaciones
        config = new NotificacionConfig();
        config.setUsuario(usuario);
        config.setEmailActivas(true);
        config.setEmailDiasAntes(3);
        config.setInAppActivas(true);
        config.setInAppDiasAntes(3);

        // Mock del repositorio de pagos
        when(pagoRepository.findPagosWithUsuarioByFechaBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(pago));

        // Mock del repositorio de configuración de notificaciones
        when(notificacionConfigRepository.findByUsuarioId(anyInt()))
                .thenReturn(Optional.of(config));

        // Mock del metodo generarPlantillaHtml para devolver un HTML válido
        when(emailNotifService.generarPlantillaHtml(any(Pago.class)))
                .thenReturn("<html>Contenido HTML del correo</html>");
    }



    @Test
    void testEnviarNotificacionesDePagos() {
        notifScheduler.enviarNotificacionesDePagos();

        verify(emailNotifService, times(1))
                .enviarCorreoSiNoEnviado(
                        eq(1), // usuarioId
                        eq(1), // pagoId
                        eq("Recordatorio de pago: Pago Mensual"), // asunto
                        eq("<html>Contenido HTML del correo</html>") // mensajeHtml
                );
    }

}
