package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.model.notif.EmailNotif;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionConfig;
import com.nextstep.nextstepBackEnd.repository.EmailNotifRepository;
import com.nextstep.nextstepBackEnd.repository.NotificacionConfigRepository;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotifScheduler {

    private final PagoRepository pagoRepository;
    private final EmailNotifService emailNotifService;
    private final InAppNotifService inAppNotifService;
    private final NotificacionConfigRepository notificacionConfigRepository;
    private final EmailNotifRepository emailNotifRepository;

    public NotifScheduler(PagoRepository pagoRepository, EmailNotifService emailNotifService,
                          InAppNotifService inAppNotifService,
                          NotificacionConfigRepository notificacionConfigRepository,
                          EmailNotifRepository emailNotifRepository) {
        this.pagoRepository = pagoRepository;
        this.emailNotifService = emailNotifService;
        this.inAppNotifService = inAppNotifService;
        this.notificacionConfigRepository = notificacionConfigRepository;
        this.emailNotifRepository = emailNotifRepository;
    }


    //@Scheduled(cron = "0 0 * * * ?") // Programar para ejecutar cada hora
    @Scheduled(cron = "0 * * * * ?") // Ejecutar cada minuto
    public void enviarNotificacionesDePagos() {
        LocalDate hoy = LocalDate.now();

        // Obtener pagos en el rango configurado para notificaciones
        List<Pago> pagosProximos = pagoRepository.findPagosWithUsuarioByFechaBetween(hoy, hoy.plusDays(7));

        for (Pago pago : pagosProximos) {
            try {
                Usuario usuario = pago.getUsuario();
                if (usuario == null) {
                    throw new IllegalStateException("El usuario asociado al pago es nulo.");
                }

                // Cargar configuración de notificaciones del usuario
                NotificacionConfig config = notificacionConfigRepository
                        .findByUsuarioId(usuario.getId())
                        .orElseThrow(() -> new IllegalStateException("Configuración de notificaciones no encontrada para el usuario ID: " + usuario.getId()));

                // Evaluar notificaciones por email
                if (config.isEmailActivas() && hoy.plusDays(config.getEmailDiasAntes()).equals(pago.getFecha())) {
                    if (!correoYaEnviado(usuario.getId(), pago.getId(), pago.getFecha())) {
                        enviarNotificacionEmail(pago, config);
                    }
                }

                // Evaluar notificaciones In-App
                if (config.isInAppActivas() && hoy.plusDays(config.getInAppDiasAntes()).equals(pago.getFecha())) {
                    enviarNotificacionInApp(pago, config, pago.getFecha());
                }

            } catch (Exception e) {
                System.err.println("Error al enviar notificación para el pago ID: " + pago.getId() + " - " + e.getMessage());
            }
        }
    }

    private void enviarNotificacionEmail(Pago pago, NotificacionConfig config) {
        try {
            String asunto = "Recordatorio de pago: " + pago.getNombre();
            String mensajeHtml = emailNotifService.generarPlantillaHtml(pago);

            // Enviar correo
            emailNotifService.enviarCorreoSiNoEnviado(pago.getUsuario().getId(), pago.getId(), asunto, mensajeHtml);
        } catch (Exception e) {
            System.err.println("Error al enviar correo para el pago ID: " + pago.getId() + " - " + e.getMessage());
        }
    }

    private boolean correoYaEnviado(Integer usuarioId, Integer pagoId, LocalDate fechaPago) {
        return emailNotifRepository.existsByUsuarioIdAndPagoIdAndFechaEnvio(usuarioId, pagoId, fechaPago.atStartOfDay());
    }




    private void enviarNotificacionInApp(Pago pago, NotificacionConfig config, LocalDate fechaPago) {
        try {
            String titulo = "Recordatorio de pago";
            String mensaje = "Tienes un pago programado para el " + pago.getFecha() + ": " + pago.getNombre();

            inAppNotifService.crearNotificacion(pago.getUsuario().getId(), pago.getId(), titulo, mensaje, fechaPago.atStartOfDay());
        } catch (Exception e) {
            System.err.println("Error al enviar notificación In-App para el pago ID: " + pago.getId() + " - " + e.getMessage());
        }
    }
}