package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.notif.Notificacion;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionDTO;
import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.NotificacionRepository;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import com.nextstep.nextstepBackEnd.service.notif.InAppNotifService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class InAppNotifServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private InAppNotifService inAppNotifService;

    private Usuario usuario;
    private Pago pago;
    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("testuser");

        pago = new Pago();
        pago.setId(1);
        pago.setUsuario(usuario);
        pago.setNombre("Pago Mensual");
        pago.setMonto(BigDecimal.valueOf(50.00));

        notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setUsuario(usuario);
        notificacion.setPago(pago);
        notificacion.setTitulo("Recordatorio");
        notificacion.setMensaje("Pago programado para mañana.");
        notificacion.setLeido(false);
        notificacion.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void convertirADTO() {
        NotificacionDTO dto = inAppNotifService.convertirADTO(notificacion);

        assertNotNull(dto);
        assertEquals(notificacion.getId(), dto.getId());
        assertEquals(notificacion.getTitulo(), dto.getTitulo());
        assertEquals(notificacion.getMensaje(), dto.getMensaje());
        assertEquals(notificacion.getLeido(), dto.getLeido());
        assertEquals(notificacion.getFechaCreacion(), dto.getFechaCreacion());
        assertEquals(notificacion.getPago().getId(), dto.getPagoId());
    }


    @Test
    void crearNotificacion() {
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Notificacion result = inAppNotifService.crearNotificacion(1, 1, "Recordatorio", "Pago programado para mañana.");

        assertNotNull(result);
        assertEquals("Recordatorio", result.getTitulo());
        verify(pagoRepository, times(1)).findById(1);
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void obtenerNotificacionesPorUsuario() {
        when(notificacionRepository.findByUsuarioId(1)).thenReturn(List.of(notificacion));

        List<Notificacion> notificaciones = inAppNotifService.obtenerNotificacionesPorUsuario(1);

        assertNotNull(notificaciones);
        assertEquals(1, notificaciones.size());
        assertEquals("Recordatorio", notificaciones.get(0).getTitulo());
        verify(notificacionRepository, times(1)).findByUsuarioId(1);
    }


    @Test
    void marcarComoLeida() {
        // Simula que findById devuelve la notificación correcta
        when(notificacionRepository.findById(1)).thenReturn(Optional.of(notificacion));

        // Simula que save devuelve la notificación actualizada
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(invocation -> {
            Notificacion saved = invocation.getArgument(0);
            saved.setLeido(true);
            saved.setFechaLeido(LocalDateTime.now());
            return saved;
        });

        // Invoca el metodo del servicio
        Notificacion result = inAppNotifService.marcarComoLeida(1);

        // Verifica los resultados
        assertNotNull(result);
        assertTrue(result.getLeido());
        assertNotNull(result.getFechaLeido());

        // Verifica que los mocks se llamaron como se esperaba
        verify(notificacionRepository, times(1)).findById(1);
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }



    @Test
    void contarNotificacionesNoLeidas() {
        when(notificacionRepository.countByUsuarioIdAndLeidoFalse(1)).thenReturn(5L);

        long count = inAppNotifService.contarNotificacionesNoLeidas(1);

        assertEquals(5L, count);
        verify(notificacionRepository, times(1)).countByUsuarioIdAndLeidoFalse(1);
    }


    @Test
    void eliminarNotificacion() {
        when(notificacionRepository.existsById(1)).thenReturn(true);

        inAppNotifService.eliminarNotificacion(1);

        verify(notificacionRepository, times(1)).existsById(1);
        verify(notificacionRepository, times(1)).deleteById(1);
    }

    @Test
    void eliminarNotificacion_NotFound() {
        when(notificacionRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            inAppNotifService.eliminarNotificacion(1);
        });

        assertEquals("Notificación no encontrada con ID: 1", exception.getMessage());
        verify(notificacionRepository, times(1)).existsById(1);
        verify(notificacionRepository, never()).deleteById(any());
    }

}

