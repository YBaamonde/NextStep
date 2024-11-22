package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.Pago;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailNotifService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailNotifService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Metodo para enviar correos en formato HTML con imágenes embebidas
    public void enviarEmailHtml(String destinatario, String asunto, String mensajeHtml) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // `true` permite adjuntos y recursos embebidos

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(mensajeHtml, true); // Activar HTML

        mailSender.send(mimeMessage);
    }


    // Metodo para generar la plantilla HTML del correo
    public String generarPlantillaHtml(Pago pago) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>" +
                "<h1 style='text-align: center; color: #FF5722;'>NextStep</h1>" +
                "<p>Hola " + pago.getUsuario().getUsername() + ",</p>" +
                "<p>Este es un recordatorio de que tienes un pago programado para mañana:</p>" +
                "<ul>" +
                "<li><strong>Pago:</strong> " + pago.getNombre() + "</li>" +
                "<li><strong>Monto:</strong> $" + pago.getMonto() + "</li>" +
                "</ul>" +
                "<p>Asegúrate de realizarlo a tiempo para evitar inconvenientes.</p>" +
                "<p>Gracias por confiar en <strong>NextStep</strong>.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
