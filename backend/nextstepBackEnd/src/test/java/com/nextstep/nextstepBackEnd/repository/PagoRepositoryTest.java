package com.nextstep.nextstepBackEnd.repository;



import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class PagoRepositoryTest {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private UserRepository userRepository;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("testuser@example.com");
        usuario.setPassword("password123");
        usuario = userRepository.save(usuario);
    }

    @Test
    public void testFindByUsuarioId() {
        Pago pago = new Pago();
        pago.setUsuario(usuario);
        pago.setNombre("Suscripción Mensual");
        pago.setMonto(BigDecimal.valueOf(15.00));
        pago.setFecha(LocalDate.now());
        pago.setRecurrente(true);
        pago.setFrecuencia(Pago.Frecuencia.MENSUAL);
        pagoRepository.save(pago);

        List<Pago> pagos = pagoRepository.findByUsuarioId(usuario.getId());

        assertNotNull(pagos);
        assertFalse(pagos.isEmpty());
        assertEquals(1, pagos.size());
        assertEquals("Suscripción Mensual", pagos.get(0).getNombre());
    }

    @Test
    public void testFindByUsuarioIdAndRecurrenteTrue() {
        Pago pago = new Pago();
        pago.setUsuario(usuario);
        pago.setNombre("Pago Anual");
        pago.setMonto(BigDecimal.valueOf(100.00));
        pago.setFecha(LocalDate.now());
        pago.setRecurrente(true);
        pago.setFrecuencia(Pago.Frecuencia.ANUAL);
        pagoRepository.save(pago);

        List<Pago> pagosRecurrentes = pagoRepository.findByUsuarioIdAndRecurrenteTrue(usuario.getId());

        assertNotNull(pagosRecurrentes);
        assertFalse(pagosRecurrentes.isEmpty());
        assertEquals(1, pagosRecurrentes.size());
        assertEquals("Pago Anual", pagosRecurrentes.get(0).getNombre());
    }
}
