package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.Notificacion;
import com.nextstep.nextstepBackEnd.model.NotificacionDTO;
import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.NotificacionRepository;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InAppNotifService {

    private final NotificacionRepository notificacionRepository;
    private final PagoRepository pagoRepository;
    private final UserRepository userRepository;

    public InAppNotifService(NotificacionRepository notificacionRepository, PagoRepository pagoRepository, UserRepository userRepository) {
        this.notificacionRepository = notificacionRepository;
        this.pagoRepository = pagoRepository;
        this.userRepository = userRepository;
    }

    // Crear una nueva notificación asociada a un pago
    @Transactional
    public Notificacion crearNotificacion(Integer usuarioId, Integer pagoId, String titulo, String mensaje) {
        // Buscar si ya existe una notificación activa (no leída) para este usuario, pago, título y mensaje
        Optional<Notificacion> notificacionExistente = notificacionRepository.findFirstByUsuarioIdAndPagoIdAndTituloAndMensajeAndLeidoFalse(
                usuarioId, pagoId, titulo, mensaje);

        if (notificacionExistente.isPresent()) {
            // Si existe una notificación no leída, no hacemos nada
            return notificacionExistente.get();
        }

        // Crear una nueva notificación si no existe ninguna activa
        Notificacion nuevaNotificacion = Notificacion.builder()
                .usuario(userRepository.findById(usuarioId)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado.")))
                .pago(pagoRepository.findById(pagoId)
                        .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado.")))
                .titulo(titulo)
                .mensaje(mensaje)
                .leido(false)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return notificacionRepository.save(nuevaNotificacion);
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
        // Obtener solo notificaciones no leídas
        return notificacionRepository.findByUsuarioIdAndLeidoFalse(usuarioId);
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
        notificacion.setFechaLeido(LocalDateTime.now()); // Actualiza el tiempo
        return notificacionRepository.save(notificacion); // GUARDA la notificación existente
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
