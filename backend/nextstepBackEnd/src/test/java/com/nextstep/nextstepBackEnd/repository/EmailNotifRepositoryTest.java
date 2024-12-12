package com.nextstep.nextstepBackEnd.repository;

import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.model.notif.EmailNotif;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@ActiveProfiles("test")
public class EmailNotifRepositoryTest {

    @Autowired
    private EmailNotifRepository emailNotifRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PagoRepository pagoRepository;

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
    public void testFindFirstByUsuarioIdAndPagoIdAndAsunto() {
        EmailNotif emailNotif = EmailNotif.builder()
                .usuario(usuario)
                .pago(pago)
                .asunto("Recordatorio de pago")
                .mensaje("Mensaje de prueba")
                .fechaEnvio(LocalDateTime.now())
                .build();
        emailNotifRepository.save(emailNotif);

        Optional<EmailNotif> result = emailNotifRepository.findFirstByUsuarioIdAndPagoIdAndAsunto(
                usuario.getId(), pago.getId(), "Recordatorio de pago");

        assertTrue(result.isPresent());
        assertEquals("Recordatorio de pago", result.get().getAsunto());
    }
}
