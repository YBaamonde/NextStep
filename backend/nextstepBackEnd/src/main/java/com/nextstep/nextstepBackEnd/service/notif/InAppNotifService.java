package com.nextstep.nextstepBackEnd.service.notif;

import com.nextstep.nextstepBackEnd.model.notif.InAppNotif;
import com.nextstep.nextstepBackEnd.model.notif.InAppNotifDTO;
import com.nextstep.nextstepBackEnd.repository.InAppNotifRepository;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InAppNotifService {
    private static final Logger logger = LoggerFactory.getLogger(InAppNotifService.class);

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
    public InAppNotif crearNotificacion(Integer usuarioId, Integer pagoId, String titulo, String mensaje, LocalDateTime fechaPago) {
        // Buscar si ya existe una notificación activa para el mismo usuario, pago, título y fecha
        Optional<InAppNotif> notificacionExistente = inAppNotifRepository.findFirstByUsuarioIdAndPagoIdAndTituloAndMensajeAndFechaCreacion(
                usuarioId, pagoId, titulo, mensaje, fechaPago);

        if (notificacionExistente.isPresent()) {
            // Si existe una notificación, no creamos una nueva
            //System.out.println("Notificación ya existe para usuarioId: " + usuarioId + ", pagoId: " + pagoId);
            logger.info("Notificación ya existe para usuarioId: " + usuarioId + ", pagoId: " + pagoId);
            return notificacionExistente.get();
        }

        // Crear una nueva notificación si no existe ninguna para esta combinación
        InAppNotif nuevaNotificacion = InAppNotif.builder()
                .usuario(userRepository.findById(usuarioId)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado.")))
                .pago(pagoRepository.findById(pagoId)
                        .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado.")))
                .titulo(titulo)
                .mensaje(mensaje)
                .leido(false)
                .fechaCreacion(fechaPago.truncatedTo(ChronoUnit.SECONDS)) // Truncar a segundos
                .build();


        return inAppNotifRepository.save(nuevaNotificacion);
    }





    // Convertir a NotificacionDTO
    public InAppNotifDTO convertirADTO(InAppNotif inAppNotif) {
        InAppNotifDTO dto = new InAppNotifDTO();
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
