package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.notif.InAppNotif;
import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.model.notif.InAppNotifDTO;
import com.nextstep.nextstepBackEnd.repository.InAppNotifRepository;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.service.notif.InAppNotifService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class InAppNotifServiceTest {

    @Mock
    private InAppNotifRepository inAppNotifRepository;

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private UserRepository userRepository;

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
        inAppNotif.setFechaCreacion(LocalDateTime.now().withNano(0));
    }

    @Test
    void convertirADTO() {
        InAppNotifDTO dto = inAppNotifService.convertirADTO(inAppNotif);

        assertNotNull(dto);
        assertEquals(inAppNotif.getId(), dto.getId());
        assertEquals(inAppNotif.getTitulo(), dto.getTitulo());
        assertEquals(inAppNotif.getMensaje(), dto.getMensaje());
        assertEquals(inAppNotif.getLeido(), dto.getLeido());
        assertEquals(inAppNotif.getFechaCreacion(), dto.getFechaCreacion());
        assertEquals(inAppNotif.getPago().getId(), dto.getPagoId());
    }

    @Test
    void crearNotificacionConFechaPago() {
        // Configurar mocks
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));
        when(inAppNotifRepository.save(any(InAppNotif.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutar el metodo con fecha específica
        LocalDateTime fechaPago = LocalDateTime.now().withNano(0);
        InAppNotif resultado = inAppNotifService.crearNotificacion(1, 1, "Recordatorio", "Pago programado para mañana.", fechaPago);

        // Validar los resultados
        assertNotNull(resultado);
        assertEquals("Recordatorio", resultado.getTitulo());
        assertEquals(fechaPago, resultado.getFechaCreacion());
        verify(userRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).findById(1);
        verify(inAppNotifRepository, times(1)).save(any(InAppNotif.class));
    }

    @Test
    void obtenerNotificacionesPorUsuario() {
        when(inAppNotifRepository.findByUsuarioIdAndLeidoFalse(1)).thenReturn(List.of(inAppNotif));

        List<InAppNotif> notificaciones = inAppNotifService.obtenerNotificacionesPorUsuario(1);

        assertNotNull(notificaciones);
        assertEquals(1, notificaciones.size());
        assertEquals("Recordatorio", notificaciones.get(0).getTitulo());
        verify(inAppNotifRepository, times(1)).findByUsuarioIdAndLeidoFalse(1);
    }

    @Test
    void contarNotificacionesNoLeidas() {
        when(inAppNotifRepository.countByUsuarioIdAndLeidoFalse(1)).thenReturn(5L);

        long count = inAppNotifService.contarNotificacionesNoLeidas(1);

        assertEquals(5L, count);
        verify(inAppNotifRepository, times(1)).countByUsuarioIdAndLeidoFalse(1);
    }

    @Test
    void marcarComoLeida() {
        when(inAppNotifRepository.findById(1)).thenReturn(Optional.of(inAppNotif));
        when(inAppNotifRepository.save(any(InAppNotif.class))).thenAnswer(invocation -> {
            InAppNotif notif = invocation.getArgument(0);
            notif.setLeido(true);
            notif.setFechaLeido(LocalDateTime.now());
            return notif;
        });

        InAppNotif resultado = inAppNotifService.marcarComoLeida(1);

        assertNotNull(resultado);
        assertTrue(resultado.getLeido());
        assertNotNull(resultado.getFechaLeido());
        verify(inAppNotifRepository, times(1)).findById(1);
        verify(inAppNotifRepository, times(1)).save(any(InAppNotif.class));
    }

    @Test
    void eliminarNotificacion() {
        when(inAppNotifRepository.existsById(1)).thenReturn(true);
        doNothing().when(inAppNotifRepository).deleteById(1);

        inAppNotifService.eliminarNotificacion(1);

        verify(inAppNotifRepository, times(1)).existsById(1);
        verify(inAppNotifRepository, times(1)).deleteById(1);
    }

    @Test
    void eliminarNotificacion_NoEncontrada() {
        when(inAppNotifRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            inAppNotifService.eliminarNotificacion(1);
        });

        assertEquals("Notificación no encontrada con ID: 1", exception.getMessage());
        verify(inAppNotifRepository, times(1)).existsById(1);
        verify(inAppNotifRepository, never()).deleteById(any());
    }
}
