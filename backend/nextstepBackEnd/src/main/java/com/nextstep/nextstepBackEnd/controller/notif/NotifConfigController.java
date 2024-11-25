package com.nextstep.nextstepBackEnd.controller.notif;

import com.nextstep.nextstepBackEnd.model.notif.NotificacionConfig;
import com.nextstep.nextstepBackEnd.service.notif.NotifConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificaciones/config")
public class NotifConfigController {

    private final NotifConfigService notifConfigService;

    public NotifConfigController(NotifConfigService notifConfigService) {
        this.notifConfigService = notifConfigService;
    }

   // Obtener configuración
    @GetMapping("/{usuarioId}")
    public ResponseEntity<NotificacionConfig> obtenerConfiguracion(@PathVariable Integer usuarioId) {
        return notifConfigService.obtenerConfiguracionPorUsuario(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<NotificacionConfig> guardarConfiguracion(@RequestBody NotificacionConfig config) {
        return ResponseEntity.ok(notifConfigService.guardarConfiguracion(config));
    }
}