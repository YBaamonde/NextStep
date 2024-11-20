package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.Notificacion;
import com.nextstep.nextstepBackEnd.model.NotificacionDTO;
import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.NotificacionRepository;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InAppNotifService {

    private final NotificacionRepository notificacionRepository;
    private final PagoRepository pagoRepository;

    public InAppNotifService(NotificacionRepository notificacionRepository, PagoRepository pagoRepository) {
        this.notificacionRepository = notificacionRepository;
        this.pagoRepository = pagoRepository;
    }

    // Crear una nueva notificación asociada a un pago
    @Transactional
    public Notificacion crearNotificacion(Integer usuarioId, Integer pagoId, String titulo, String mensaje) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado con ID: " + pagoId));

        // Validar que el pago pertenece al usuario
        if (!pago.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("El pago no pertenece al usuario especificado.");
        }

        Notificacion notificacion = Notificacion.builder()
                .usuario(pago.getUsuario())
                .pago(pago)
                .titulo(titulo)
                .mensaje(mensaje)
                .leido(false)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return notificacionRepository.save(notificacion);
    }

    // Convertir a NotificacionDTO
    public NotificacionDTO convertirADTO(Notificacion notificacion) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(notificacion.getId());
        dto.setTitulo(notificacion.getTitulo());
        dto.setMensaje(notificacion.getMensaje());
        dto.setLeido(notificacion.getLeido());
        dto.setFechaCreacion(notificacion.getFechaCreacion());
        dto.setFechaLeido(notificacion.getFechaLeido());
        dto.setPagoId(notificacion.getPago().getId());
        return dto;
    }


    // Obtener todas las notificaciones de un usuario
    public List<Notificacion> obtenerNotificacionesPorUsuario(Integer usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    // Contar las notificaciones no leídas de un usuario
    public long contarNotificacionesNoLeidas(Integer usuarioId) {
        return notificacionRepository.countByUsuarioIdAndLeidoFalse(usuarioId);
    }

    // Marcar una notificación como leída
    @Transactional
    public Notificacion marcarComoLeida(Integer notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada con ID: " + notificacionId));

        notificacion.setLeido(true);
        notificacion.setFechaLeido(LocalDateTime.now());
        return notificacionRepository.save(notificacion);
    }


    // Eliminar una notificación
    @Transactional
    public void eliminarNotificacion(Integer notificacionId) {
        if (notificacionRepository.existsById(notificacionId)) {
            notificacionRepository.deleteById(notificacionId);
        } else {
            throw new IllegalArgumentException("Notificación no encontrada con ID: " + notificacionId);
        }
    }
}
