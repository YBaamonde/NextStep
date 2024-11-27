package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.notif.InAppNotif;
import com.nextstep.nextstepBackEnd.model.notif.NotificacionDTO;
import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.InAppNotifRepository;
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
    private InAppNotifRepository inAppNotifRepository;

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private InAppNotifService inAppNotifService;

    private Usuario usuario;
    private Pago pago;
    private InAppNotif inAppNotif;

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

        inAppNotif = new InAppNotif();
        inAppNotif.setId(1);
        inAppNotif.setUsuario(usuario);
        inAppNotif.setPago(pago);
        inAppNotif.setTitulo("Recordatorio");
        inAppNotif.setMensaje("Pago programado para mañana.");
        inAppNotif.setLeido(false);
        inAppNotif.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void convertirADTO() {
        NotificacionDTO dto = inAppNotifService.convertirADTO(inAppNotif);

        assertNotNull(dto);
        assertEquals(inAppNotif.getId(), dto.getId());
        assertEquals(inAppNotif.getTitulo(), dto.getTitulo());
        assertEquals(inAppNotif.getMensaje(), dto.getMensaje());
        assertEquals(inAppNotif.getLeido(), dto.getLeido());
        assertEquals(inAppNotif.getFechaCreacion(), dto.getFechaCreacion());
        assertEquals(inAppNotif.getPago().getId(), dto.getPagoId());
    }


    @Test
    void crearNotificacion() {
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));
        when(inAppNotifRepository.save(any(InAppNotif.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InAppNotif result = inAppNotifService.crearNotificacion(1, 1, "Recordatorio", "Pago programado para mañana.");

        assertNotNull(result);
        assertEquals("Recordatorio", result.getTitulo());
        verify(pagoRepository, times(1)).findById(1);
        verify(inAppNotifRepository, times(1)).save(any(InAppNotif.class));
    }

    @Test
    void obtenerNotificacionesPorUsuario() {
        when(inAppNotifRepository.findByUsuarioId(1)).thenReturn(List.of(inAppNotif));

        List<InAppNotif> notificaciones = inAppNotifService.obtenerNotificacionesPorUsuario(1);

        assertNotNull(notificaciones);
        assertEquals(1, notificaciones.size());
        assertEquals("Recordatorio", notificaciones.get(0).getTitulo());
        verify(inAppNotifRepository, times(1)).findByUsuarioId(1);
    }


    @Test
    void marcarComoLeida() {
        // Simula que findById devuelve la notificación correcta
        when(inAppNotifRepository.findById(1)).thenReturn(Optional.of(inAppNotif));

        // Simula que save devuelve la notificación actualizada
        when(inAppNotifRepository.save(any(InAppNotif.class))).thenAnswer(invocation -> {
            InAppNotif saved = invocation.getArgument(0);
            saved.setLeido(true);
            saved.setFechaLeido(LocalDateTime.now());
            return saved;
        });

        // Invoca el metodo del servicio
        InAppNotif result = inAppNotifService.marcarComoLeida(1);

        // Verifica los resultados
        assertNotNull(result);
        assertTrue(result.getLeido());
        assertNotNull(result.getFechaLeido());

        // Verifica que los mocks se llamaron como se esperaba
        verify(inAppNotifRepository, times(1)).findById(1);
        verify(inAppNotifRepository, times(1)).save(any(InAppNotif.class));
    }



    @Test
    void contarNotificacionesNoLeidas() {
        when(inAppNotifRepository.countByUsuarioIdAndLeidoFalse(1)).thenReturn(5L);

        long count = inAppNotifService.contarNotificacionesNoLeidas(1);

        assertEquals(5L, count);
        verify(inAppNotifRepository, times(1)).countByUsuarioIdAndLeidoFalse(1);
    }


    @Test
    void eliminarNotificacion() {
        when(inAppNotifRepository.existsById(1)).thenReturn(true);

        inAppNotifService.eliminarNotificacion(1);

        verify(inAppNotifRepository, times(1)).existsById(1);
        verify(inAppNotifRepository, times(1)).deleteById(1);
    }

    @Test
    void eliminarNotificacion_NotFound() {
        when(inAppNotifRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            inAppNotifService.eliminarNotificacion(1);
        });

        assertEquals("Notificación no encontrada con ID: 1", exception.getMessage());
        verify(inAppNotifRepository, times(1)).existsById(1);
        verify(inAppNotifRepository, never()).deleteById(any());
    }

}

