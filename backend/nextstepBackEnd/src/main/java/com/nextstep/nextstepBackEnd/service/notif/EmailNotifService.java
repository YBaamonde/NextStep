package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.model.notif.EmailNotif;
import com.nextstep.nextstepBackEnd.repository.EmailNotifRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailNotifService {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotifService.class);

    private final JavaMailSender mailSender;
    private final EmailNotifRepository emailNotifRepository; // Repositorio para gestionar correos enviados
    private final UserRepository userRepository;
    private final PagoRepository pagoRepository;

    @Autowired
    public EmailNotifService(JavaMailSender mailSender,
                             EmailNotifRepository emailNotifRepository,
                             UserRepository userRepository,
                             PagoRepository pagoRepository) {
        this.mailSender = mailSender;
        this.emailNotifRepository = emailNotifRepository;
        this.userRepository = userRepository;
        this.pagoRepository = pagoRepository;
    }

    public void enviarEmailHtml(String destinatario, String asunto, String mensajeHtml) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // `true` permite adjuntos y recursos embebidos

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(mensajeHtml, true); // Activar HTML

        mailSender.send(mimeMessage);
    }

    public String generarPlantillaHtml(Pago pago) {
        String montoFormateado = String.format(Locale.US, "%.2f", pago.getMonto()); // Forzar el formato con punto decimal

        return "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>" +
                "<h1 style='text-align: center; color: #FF5722;'>NextStep</h1>" +
                "<p>Hola " + pago.getUsuario().getUsername() + ",</p>" +
                "<p>Este es un recordatorio de que tienes un pago programado para mañana:</p>" +
                "<ul>" +
                "<li><strong>Pago:</strong> " + pago.getNombre() + "</li>" +
                "<li><strong>Monto:</strong> $" + montoFormateado + "</li>" +
                "</ul>" +
                "<p>Asegúrate de realizarlo a tiempo para evitar inconvenientes.</p>" +
                "<p>Gracias por confiar en <strong>NextStep</strong>.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }


    @Transactional
    public boolean enviarCorreoSiNoEnviado(Integer usuarioId, Integer pagoId, String asunto, String mensajeHtml) {
        // Buscar si ya existe un registro de correo enviado
        Optional<EmailNotif> notificacionExistente = emailNotifRepository.findFirstByUsuarioIdAndPagoIdAndAsunto(
                usuarioId, pagoId, asunto);

        if (notificacionExistente.isPresent()) {
            // Si ya existe, no enviamos el correo nuevamente
            //System.out.println("Correo ya enviado para usuario ID: " + usuarioId + ", pago ID: " + pagoId);
            logger.info("Correo ya enviado para usuario ID: " + usuarioId + ", pago ID: " + pagoId);
            return false;
        }

        // Si no existe, enviamos el correo
        try {
            Usuario usuario = userRepository.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
            enviarEmailHtml(usuario.getEmail(), asunto, mensajeHtml);

            // Registrar el envío del correo
            EmailNotif nuevaNotificacion = EmailNotif.builder()
                    .usuario(usuario)
                    .pago(pagoRepository.findById(pagoId)
                            .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado.")))
                    .asunto(asunto)
                    .mensaje(mensajeHtml)
                    .fechaEnvio(LocalDateTime.now())
                    .build();

            emailNotifRepository.save(nuevaNotificacion);
            return true;
        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            return false;
        }
    }
}
