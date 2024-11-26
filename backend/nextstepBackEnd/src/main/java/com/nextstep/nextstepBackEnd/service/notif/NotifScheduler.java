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
    // Programar para ejecutar todos los días a las 8:00 AM
    //@Scheduled(cron = "0 0 8 * * ?")
    public void enviarNotificacionesDePagos() {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = inicio.plusDays(7); // Configura el rango de fechas si es necesario

        // Obtener pagos
        List<Pago> pagosProximos = pagoRepository.findPagosWithUsuarioByFechaBetween(inicio, fin);


        for (Pago pago : pagosProximos) {
            try {
                // Forzar la inicialización de Usuario
                Usuario usuario = pago.getUsuario();
                if (usuario == null) {
                    throw new IllegalStateException("El usuario asociado al pago es nulo.");
                }

                // Acceder a las propiedades del usuario para asegurar su inicialización
                String email = usuario.getEmail();
                String username = usuario.getUsername();

                // Notificación por correo
                String asunto = "Recordatorio de pago: " + pago.getNombre();
                String mensajeHtml = "<html><body>Recordatorio de pago: " + pago.getNombre() + "</body></html>";
                emailNotifService.enviarEmailHtml(email, asunto, mensajeHtml);

                // Notificación In-App
                String titulo = "Recordatorio de pago";
                String mensaje = "Tienes un pago programado para el " + pago.getFecha() + ": " + pago.getNombre();
                inAppNotifService.crearNotificacion(usuario.getId(), pago.getId(), titulo, mensaje);

            } catch (Exception e) {
                System.err.println("Error al enviar notificación para el pago ID: " + pago.getId() + " - " + e.getMessage());
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
