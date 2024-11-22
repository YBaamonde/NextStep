package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class EmailNotifScheduler {

    private final PagoRepository pagoRepository;
    private final EmailNotifService emailNotifService;

    public EmailNotifScheduler(PagoRepository pagoRepository, EmailNotifService emailNotifService) {
        this.pagoRepository = pagoRepository;
        this.emailNotifService = emailNotifService;
    }

    // Programar este metodo para ejecutarse todos los días a las 8:00 AM
    //@Scheduled(cron = "0 0 8 * * ?") // Formato CRON para ejecutar a las 8:00 AM
    @Scheduled(cron = "0 * * * * ?") // Ejecutar cada minuto para pruebas
    public void enviarNotificacionesDePagos() {
        // Obtener fecha del día siguiente
        LocalDate fechaManana = LocalDate.now().plusDays(1);

        // Buscar todos los pagos con fecha igual a "mañana"
        List<Pago> pagosProximos = pagoRepository.findByFechaWithUsuario(fechaManana);

        // Enviar correo por cada pago
        for (Pago pago : pagosProximos) {
            try {
                String asunto = "Recordatorio de pago: " + pago.getNombre();

                // Generar el HTML del correo
                String mensajeHtml = emailNotifService.generarPlantillaHtml(pago);

                // Ruta del logo
                String logoPath = "src/main/resources/media/logo03.png";

                // Enviar correo con el logo embebido
                emailNotifService.enviarEmailHtml(pago.getUsuario().getEmail(), asunto, mensajeHtml);
            } catch (Exception e) {
                System.err.println("Error al enviar correo para el pago ID: " + pago.getId() + " - " + e.getMessage());
            }
        }
    }


}
