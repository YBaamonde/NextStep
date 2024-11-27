package com.nextstep.nextstepBackEnd.controller.notif;


import com.nextstep.nextstepBackEnd.service.notif.EmailNotifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
            emailNotifService.enviarEmailHtml(destinatario, asunto, mensajeHtml);
            return ResponseEntity.ok("Correo HTML enviado correctamente a " + destinatario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el correo: " + e.getMessage());
        }
    }

    // Nuevo endpoint: Verificar si un correo ya fue enviado
    @GetMapping("/email/enviado")
    public ResponseEntity<Boolean> verificarCorreoEnviado(
            @RequestParam Integer usuarioId,
            @RequestParam Integer pagoId,
            @RequestParam LocalDate fechaPago) {
        boolean enviado = emailNotifService.enviarCorreoSiNoEnviado(
                usuarioId, pagoId, "Recordatorio de pago", fechaPago.toString());
        return ResponseEntity.ok(enviado);
    }
}
