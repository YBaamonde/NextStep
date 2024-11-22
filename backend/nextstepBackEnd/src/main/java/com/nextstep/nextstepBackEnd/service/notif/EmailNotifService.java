package com.nextstep.nextstepBackEnd.service.notif;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotifService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailNotifService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Metodo para enviar notificaciones por email
    public void enviarEmail(String destinatario, String asunto, String mensaje) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(destinatario);
        mailMessage.setSubject(asunto);
        mailMessage.setText(mensaje);

        mailSender.send(mailMessage);
    }

}
