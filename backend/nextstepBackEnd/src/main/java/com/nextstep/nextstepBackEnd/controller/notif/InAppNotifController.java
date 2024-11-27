package com.nextstep.nextstepBackEnd.controller.notif;

import com.nextstep.nextstepBackEnd.model.notif.InAppNotif;
import com.nextstep.nextstepBackEnd.model.notif.InAppNotifDTO;
import com.nextstep.nextstepBackEnd.service.notif.InAppNotifService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public ResponseEntity<InAppNotifDTO> crearNotificacion(
            @RequestParam Integer usuarioId,
            @RequestParam Integer pagoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaPago, // Nuevo parámetro con formato de fecha
            @Valid @RequestBody NotificacionRequest request) {
        // Convertir LocalDate a LocalDateTime (opcional según implementación del servicio)
        LocalDateTime fechaPagoDateTime = fechaPago.atStartOfDay();

        // Llamar al servicio con los nuevos parámetros
        InAppNotif inAppNotif = inAppNotifService.crearNotificacion(
                usuarioId, pagoId, request.getTitulo(), request.getMensaje(), fechaPagoDateTime
        );

        // Convertir a DTO y retornar la respuesta
        return ResponseEntity.ok(inAppNotifService.convertirADTO(inAppNotif));
    }

    // Obtener todas las notificaciones de un usuario
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<InAppNotifDTO>> obtenerNotificaciones(@PathVariable Integer usuarioId) {
        List<InAppNotif> notificaciones = inAppNotifService.obtenerNotificacionesPorUsuario(usuarioId);
        List<InAppNotifDTO> dtos = notificaciones.stream()
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
    public ResponseEntity<InAppNotifDTO> marcarComoLeida(@PathVariable Integer notificacionId) {
        InAppNotif inAppNotif = inAppNotifService.marcarComoLeida(notificacionId);
        return ResponseEntity.ok(inAppNotifService.convertirADTO(inAppNotif));
    }
}
