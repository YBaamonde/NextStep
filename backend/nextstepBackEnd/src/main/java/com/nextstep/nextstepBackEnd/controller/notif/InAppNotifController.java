package com.nextstep.nextstepBackEnd.controller.notif;

import com.nextstep.nextstepBackEnd.model.notif.Notificacion;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionDTO;
import com.nextstep.nextstepBackEnd.service.notif.InAppNotifService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notificaciones/inapp")
public class InAppNotifController {

    private final InAppNotifService inAppNotifService;

    public InAppNotifController(InAppNotifService inAppNotifService) {
        this.inAppNotifService = inAppNotifService;
    }

    // Crear una nueva notificación asociada a un pago
    @PostMapping
    public ResponseEntity<NotificacionDTO> crearNotificacion(
            @RequestParam Integer usuarioId,
            @RequestParam Integer pagoId,
            @Valid @RequestBody NotificacionRequest request) {
        Notificacion notificacion = inAppNotifService.crearNotificacion(
                usuarioId, pagoId, request.getTitulo(), request.getMensaje()
        );
        return ResponseEntity.ok(inAppNotifService.convertirADTO(notificacion));
    }

    // Obtener todas las notificaciones de un usuario
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificaciones(@PathVariable Integer usuarioId) {
        List<Notificacion> notificaciones = inAppNotifService.obtenerNotificacionesPorUsuario(usuarioId);
        List<NotificacionDTO> dtos = notificaciones.stream()
                .map(inAppNotifService::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Contar las notificaciones no leídas de un usuario
    @GetMapping("/{usuarioId}/no-leidas")
    public ResponseEntity<Long> contarNotificacionesNoLeidas(@PathVariable Integer usuarioId) {
        long noLeidas = inAppNotifService.contarNotificacionesNoLeidas(usuarioId);
        return ResponseEntity.ok(noLeidas);
    }

    // Marcar una notificación como leída
    @PutMapping("/{notificacionId}/leida")
    public ResponseEntity<NotificacionDTO> marcarComoLeida(@PathVariable Integer notificacionId) {
        Notificacion notificacion = inAppNotifService.marcarComoLeida(notificacionId);
        return ResponseEntity.ok(inAppNotifService.convertirADTO(notificacion));
    }
}