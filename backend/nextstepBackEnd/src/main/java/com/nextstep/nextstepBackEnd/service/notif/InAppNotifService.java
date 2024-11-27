package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.notif.InAppNotif;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionDTO;
import com.nextstep.nextstepBackEnd.repository.InAppNotifRepository;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InAppNotifService {

    private final InAppNotifRepository inAppNotifRepository;
    private final PagoRepository pagoRepository;
    private final UserRepository userRepository;

    public InAppNotifService(InAppNotifRepository inAppNotifRepository, PagoRepository pagoRepository, UserRepository userRepository) {
        this.inAppNotifRepository = inAppNotifRepository;
        this.pagoRepository = pagoRepository;
        this.userRepository = userRepository;
    }

    // Crear una nueva notificación asociada a un pago
    @Transactional
    public InAppNotif crearNotificacion(Integer usuarioId, Integer pagoId, String titulo, String mensaje) {
        // Buscar si ya existe una notificación activa (no leída) para este usuario, pago, título y mensaje
        Optional<InAppNotif> notificacionExistente = inAppNotifRepository.findFirstByUsuarioIdAndPagoIdAndTituloAndMensajeAndLeidoFalse(
                usuarioId, pagoId, titulo, mensaje);

        if (notificacionExistente.isPresent()) {
            // Si existe una notificación no leída, no hacemos nada
            return notificacionExistente.get();
        }

        // Crear una nueva notificación si no existe ninguna activa
        InAppNotif nuevaInAppNotif = InAppNotif.builder()
                .usuario(userRepository.findById(usuarioId)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado.")))
                .pago(pagoRepository.findById(pagoId)
                        .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado.")))
                .titulo(titulo)
                .mensaje(mensaje)
                .leido(false)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return inAppNotifRepository.save(nuevaInAppNotif);
    }



    // Convertir a NotificacionDTO
    public NotificacionDTO convertirADTO(InAppNotif inAppNotif) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(inAppNotif.getId());
        dto.setTitulo(inAppNotif.getTitulo());
        dto.setMensaje(inAppNotif.getMensaje());
        dto.setLeido(inAppNotif.getLeido());
        dto.setFechaCreacion(inAppNotif.getFechaCreacion());
        dto.setFechaLeido(inAppNotif.getFechaLeido());
        dto.setPagoId(inAppNotif.getPago().getId());
        return dto;
    }


    // Obtener todas las notificaciones de un usuario
    public List<InAppNotif> obtenerNotificacionesPorUsuario(Integer usuarioId) {
        // Obtener solo notificaciones no leídas
        return inAppNotifRepository.findByUsuarioIdAndLeidoFalse(usuarioId);
    }


    // Contar las notificaciones no leídas de un usuario
    public long contarNotificacionesNoLeidas(Integer usuarioId) {
        return inAppNotifRepository.countByUsuarioIdAndLeidoFalse(usuarioId);
    }

    // Marcar una notificación como leída
    @Transactional
    public InAppNotif marcarComoLeida(Integer notificacionId) {
        InAppNotif inAppNotif = inAppNotifRepository.findById(notificacionId)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada con ID: " + notificacionId));

        inAppNotif.setLeido(true);
        inAppNotif.setFechaLeido(LocalDateTime.now()); // Actualiza el tiempo
        return inAppNotifRepository.save(inAppNotif); // GUARDA la notificación existente
    }


    // Eliminar una notificación
    @Transactional
    public void eliminarNotificacion(Integer notificacionId) {
        if (inAppNotifRepository.existsById(notificacionId)) {
            inAppNotifRepository.deleteById(notificacionId);
        } else {
            throw new IllegalArgumentException("Notificación no encontrada con ID: " + notificacionId);
        }
    }

}
