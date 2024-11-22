package com.nextstep.nextstepBackEnd.controller.notif;


import com.nextstep.nextstepBackEnd.service.notif.EmailNotifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificaciones/email")
public class EmailNotifController {

    private final EmailNotifService emailNotifService;

    @Autowired
    public EmailNotifController(EmailNotifService emailNotifService) {
        this.emailNotifService = emailNotifService;
    }

    // Metodo para enviar notificaciones por email
    @PostMapping
    public ResponseEntity<String> enviarEmail(
            @RequestParam String destinatario,
            @RequestParam String asunto,
            @RequestParam String mensaje
    )
    {
        emailNotifService.enviarEmail(destinatario, asunto, mensaje);
        return ResponseEntity.ok("Notificación enviada correctamente a " + destinatario);
    }


}
