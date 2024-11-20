package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Notificacion;
import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static junit.framework.TestCase.*;

@DataJpaTest
public class NotificacionRepositoryTest {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private UserRepository userRepository;

    private Usuario usuario;
    private Pago pago;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("testuser@example.com");
        usuario.setPassword("password123");
        usuario = userRepository.save(usuario);

        pago = new Pago();
        pago.setUsuario(usuario);
        pago.setNombre("Pago de Prueba");
        pago.setMonto(BigDecimal.valueOf(100.00));
        pago.setFecha(LocalDate.now());
        pago.setRecurrente(true);
        pago.setFrecuencia(Pago.Frecuencia.MENSUAL);
        pago = pagoRepository.save(pago);
    }

    @Test
    public void testFindByUsuarioId() {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setPago(pago);
        notificacion.setTitulo("Recordatorio");
        notificacion.setMensaje("Tienes un pago pendiente.");
        notificacion.setLeido(false);
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacionRepository.save(notificacion);

        List<Notificacion> notificaciones = notificacionRepository.findByUsuarioId(usuario.getId());

        assertNotNull(notificaciones);
        assertFalse(notificaciones.isEmpty());
        assertEquals(1, notificaciones.size());
        assertEquals("Recordatorio", notificaciones.get(0).getTitulo());
    }

    @Test
    public void testCountByUsuarioIdAndLeidoFalse() {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setPago(pago);
        notificacion.setTitulo("Recordatorio");
        notificacion.setMensaje("Tienes un pago pendiente.");
        notificacion.setLeido(false);
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacionRepository.save(notificacion);

        long count = notificacionRepository.countByUsuarioIdAndLeidoFalse(usuario.getId());

        assertEquals(1, count);
    }
}
