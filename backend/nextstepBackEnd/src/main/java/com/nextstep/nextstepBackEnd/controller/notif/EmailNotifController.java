package com.nextstep.nextstepBackEnd.controller.notif;


import com.nextstep.nextstepBackEnd.service.notif.EmailNotifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificaciones")
public class EmailNotifController {

    private final EmailNotifService emailNotifService;

    @Autowired
    public EmailNotifController(EmailNotifService emailNotifService) {
        this.emailNotifService = emailNotifService;
    }

    // Endpoint para enviar correos HTML usando un mensaje personalizado
    @PostMapping("/email")
    public ResponseEntity<String> enviarEmailHtml(
            @RequestParam String destinatario,
            @RequestParam String asunto,
            @RequestParam String mensajeHtml
    ) {
        try {
            String logoPath = "src/main/resources/media/logo03.png";
            emailNotifService.enviarEmailHtmlConLogo(destinatario, asunto, mensajeHtml, logoPath);
            return ResponseEntity.ok("Correo HTML enviado correctamente a " + destinatario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el correo: " + e.getMessage());
        }
    }

}