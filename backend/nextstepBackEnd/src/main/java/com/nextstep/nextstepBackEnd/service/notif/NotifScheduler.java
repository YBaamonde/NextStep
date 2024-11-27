package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.PagoDTO;
import com.nextstep.nextstepBackEnd.model.Usuario;
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
    //@Scheduled(cron = "0 0 */1 * * ?") // Programar para ejecutar cada hora
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
                    enviarNotificacionEmail(pago, config);
                }

                // Evaluar notificaciones In-App
                if (config.isInAppActivas() && hoy.plusDays(config.getInAppDiasAntes()).equals(pago.getFecha())) {
                    enviarNotificacionInApp(pago, config);
                }

                // Mensajes de log para comprobar si cambia la configuración según el usuario
                System.out.println("Configuración para usuario ID: " + usuario.getId() +
                        " - Email anticipación: " + config.getEmailDiasAntes() +
                        ", In-App anticipación: " + config.getInAppDiasAntes());
                System.out.println("Pago ID: " + pago.getId() + " con fecha: " + pago.getFecha());


            } catch (Exception e) {
                System.err.println("Error al enviar notificación para el pago ID: " + pago.getId() + " - " + e.getMessage());
            }
        }
    }

    private void enviarNotificacionEmail(Pago pago, NotificacionConfig config) {
        try {
            // Usar el metodo generarPlantillaHtml
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
