package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.notif.InAppNotif;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class InAppNotifRepositoryTest {

    @Autowired
    private InAppNotifRepository inAppNotifRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private UserRepository userRepository;

    private Usuario usuario;
    private Pago pago;
    private InAppNotif inAppNotif;

    @BeforeEach
    public void setUp() {
        // Crear y guardar un usuario
        usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("testuser@example.com");
        usuario.setPassword("password123");
        usuario = userRepository.save(usuario);

        // Crear y guardar un pago
        pago = new Pago();
        pago.setUsuario(usuario);
        pago.setNombre("Pago de Prueba");
        pago.setMonto(BigDecimal.valueOf(100.00));
        pago.setFecha(LocalDate.now());
        pago.setRecurrente(true);
        pago.setFrecuencia(Pago.Frecuencia.MENSUAL);
        pago = pagoRepository.save(pago);

        // Crear y guardar una notificación
        inAppNotif = new InAppNotif();
        inAppNotif.setUsuario(usuario);
        inAppNotif.setPago(pago);
        inAppNotif.setTitulo("Recordatorio");
        inAppNotif.setMensaje("Tienes un pago pendiente.");
        inAppNotif.setLeido(false);
        // Truncar los milisegundos para evitar problemas
        inAppNotif.setFechaCreacion(LocalDateTime.now().withNano(0));
        inAppNotifRepository.save(inAppNotif);
    }


    @Test
    public void testFindByUsuarioId() {
        List<InAppNotif> notificaciones = inAppNotifRepository.findByUsuarioId(usuario.getId());

        assertNotNull(notificaciones);
        assertFalse(notificaciones.isEmpty());
        assertEquals(1, notificaciones.size());
        assertEquals("Recordatorio", notificaciones.get(0).getTitulo());
    }

    @Test
    public void testCountByUsuarioIdAndLeidoFalse() {
        long count = inAppNotifRepository.countByUsuarioIdAndLeidoFalse(usuario.getId());

        assertEquals(1, count);
    }

    @Test
    public void testFindByUsuarioIdAndFechaCreacion() {
        // Truncar los milisegundos al buscar
        LocalDateTime fechaCreacion = inAppNotif.getFechaCreacion().withNano(0);

        Optional<InAppNotif> resultado = inAppNotifRepository.findFirstByUsuarioIdAndPagoIdAndTituloAndMensajeAndFechaCreacion(
                usuario.getId(),
                pago.getId(),
                inAppNotif.getTitulo(),
                inAppNotif.getMensaje(),
                fechaCreacion
        );

        // Verificar que la consulta encontró la notificación
        assertTrue(resultado.isPresent());
        assertEquals(inAppNotif.getId(), resultado.get().getId());
    }

}
