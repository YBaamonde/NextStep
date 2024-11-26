package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionConfig;
import com.nextstep.nextstepBackEnd.repository.NotificacionConfigRepository;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class NotifScheduler {

    private final PagoRepository pagoRepository;
    private final EmailNotifService emailNotifService;
    private final InAppNotifService inAppNotifService;
    private final NotificacionConfigRepository notificacionConfigRepository;

    public NotifScheduler(PagoRepository pagoRepository, EmailNotifService emailNotifService,
                          InAppNotifService inAppNotifService, NotificacionConfigRepository notificacionConfigRepository) {
        this.pagoRepository = pagoRepository;
        this.emailNotifService = emailNotifService;
        this.inAppNotifService = inAppNotifService;
        this.notificacionConfigRepository = notificacionConfigRepository;
    }

    // Programar para ejecutar cada minuto para pruebas
    @Scheduled(cron = "0 * * * * ?")
    // Programar para ejecutar todos los días a las 8:00 AM
    //@Scheduled(cron = "0 0 8 * * ?")
    public void enviarNotificacionesDePagos() {
        // Obtener la fecha de hoy
        LocalDate fechaHoy = LocalDate.now();

        // Buscar todos los pagos futuros (por ejemplo, en los próximos 7 días para eficiencia)
        List<Pago> pagosProximos = pagoRepository.findPagosFuturos(fechaHoy.plusDays(7));

        for (Pago pago : pagosProximos) {
            try {
                // Obtener configuración del usuario
                NotificacionConfig config = notificacionConfigRepository
                        .findByUsuarioId(pago.getUsuario().getId())
                        .orElse(new NotificacionConfig()); // Configuración por defecto si no existe

                // Calcular la diferencia en días entre hoy y la fecha del pago
                long diasRestantes = ChronoUnit.DAYS.between(fechaHoy, pago.getFecha());

                // Verificar si hay que enviar notificaciones según la configuración
                if (config.isEmailActivas() && diasRestantes == config.getEmailDiasAntes()) {
                    enviarNotificacionEmail(pago, config);
                }

                if (config.isInAppActivas() && diasRestantes == config.getInAppDiasAntes()) {
                    enviarNotificacionInApp(pago, config);
                }

            } catch (Exception e) {
                System.err.println("Error al procesar notificación para el pago ID: " + pago.getId() + " - " + e.getMessage());
            }
        }
    }

    private void enviarNotificacionEmail(Pago pago, NotificacionConfig config) {
        try {
            String asunto = "Recordatorio de pago: " + pago.getNombre();
            String mensajeHtml = emailNotifService.generarPlantillaHtml(pago);
            emailNotifService.enviarEmailHtml(pago.getUsuario().getEmail(), asunto, mensajeHtml);
            System.out.println("Correo enviado para pago ID: " + pago.getId());
        } catch (Exception e) {
            System.err.println("Error al enviar correo para el pago ID: " + pago.getId() + " - " + e.getMessage());
        }
    }

    private void enviarNotificacionInApp(Pago pago, NotificacionConfig config) {
        try {
            String titulo = "Recordatorio de pago";
            String mensaje = "Tienes un pago programado para el " + pago.getFecha() + ": " + pago.getNombre();
            inAppNotifService.crearNotificacion(pago.getUsuario().getId(), pago.getId(), titulo, mensaje);
            System.out.println("Notificación In-App enviada para pago ID: " + pago.getId());
        } catch (Exception e) {
            System.err.println("Error al enviar notificación In-App para el pago ID: " + pago.getId() + " - " + e.getMessage());
        }
    }
}
