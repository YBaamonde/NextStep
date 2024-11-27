package com.nextstep.nextstepBackEnd.controller.notif;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionConfig;
import com.nextstep.nextstepBackEnd.service.notif.NotifConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/notificaciones/config")
public class NotifConfigController {

    // Logger
    private static final Logger log = LoggerFactory.getLogger(NotifConfigController.class);

    private final NotifConfigService notifConfigService;

    public NotifConfigController(NotifConfigService notifConfigService) {
        this.notifConfigService = notifConfigService;
    }

   // Obtener configuración
   @GetMapping("/{usuarioId}")
   public ResponseEntity<NotificacionConfig> obtenerConfiguracion(@PathVariable Integer usuarioId) {
       return notifConfigService.obtenerConfiguracionPorUsuario(usuarioId)
               .map(ResponseEntity::ok)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Configuración no encontrada."));
   }


    @PutMapping
    public ResponseEntity<NotificacionConfig> guardarConfiguracion(@RequestBody NotificacionConfig config) {
        if (config.getUsuario() == null || config.getUsuario().getId() == null) {
            return ResponseEntity.badRequest().body(null); // Responder con error 400 si falta el usuario
        }

        try {
            NotificacionConfig savedConfig = notifConfigService.guardarConfiguracion(config);
            return ResponseEntity.ok(savedConfig);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Responder con 404 si el usuario no se encuentra
        }
    }



}