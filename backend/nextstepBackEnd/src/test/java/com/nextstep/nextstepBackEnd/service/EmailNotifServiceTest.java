package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.notif.EmailNotif;
import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.EmailNotifRepository;
import com.nextstep.nextstepBackEnd.repository.PagoRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import com.nextstep.nextstepBackEnd.service.notif.EmailNotifService;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
public class EmailNotifServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotifServiceTest.class);

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailNotifRepository emailNotifRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private EmailNotifService emailNotifService;

    private Usuario usuario;
    private Pago pago;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("testuser");
        usuario.setEmail("testuser@example.com");

        pago = new Pago();
        pago.setId(1);
        pago.setUsuario(usuario);
        pago.setNombre("Pago Mensual");
        pago.setMonto(BigDecimal.valueOf(100.00));
        pago.setFecha(LocalDate.now());
    }

    @Test
    void testEnviarEmailHtml() throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailNotifService.enviarEmailHtml("testuser@example.com", "Asunto", "<h1>Mensaje</h1>");

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testGenerarPlantillaHtml() {
        String html = emailNotifService.generarPlantillaHtml(pago);

        // Imprimir el HTML generado para depuración
        //System.out.println("HTML generado:\n" + html);
        logger.info("HTML generado:\n" + html);

        // Verifica que el HTML no sea nulo y contenga las cadenas esperadas
        assertNotNull(html, "El HTML generado no debe ser nulo");
        assertTrue(html.contains("NextStep"), "El HTML debe contener el título 'NextStep'");
        assertTrue(html.contains("Hola testuser"), "El HTML debe contener 'Hola testuser'");
        assertTrue(html.contains("Pago Mensual"), "El HTML debe contener el nombre del pago 'Pago Mensual'");
        assertTrue(html.contains("$100.00"), "El HTML debe contener el monto '$100.00'");
    }



    @Test
    void testEnviarCorreoSiNoEnviado_CorreoNoEnviado() throws MessagingException {
        // Crear un MimeMessage simulado
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(emailNotifRepository.findFirstByUsuarioIdAndPagoIdAndAsunto(1, 1, "Recordatorio de pago")).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));

        boolean result = emailNotifService.enviarCorreoSiNoEnviado(1, 1, "Recordatorio de pago", "<h1>Recordatorio</h1>");

        assertTrue(result); // El correo debería haberse enviado correctamente
        verify(emailNotifRepository, times(1)).save(any(EmailNotif.class));
    }


    @Test
    void testEnviarCorreoSiNoEnviado_CorreoYaEnviado() {
        EmailNotif notificacionExistente = new EmailNotif();
        notificacionExistente.setId(1);
        notificacionExistente.setUsuario(usuario);
        notificacionExistente.setPago(pago);
        notificacionExistente.setAsunto("Recordatorio de pago");

        when(emailNotifRepository.findFirstByUsuarioIdAndPagoIdAndAsunto(1, 1, "Recordatorio de pago")).thenReturn(Optional.of(notificacionExistente));

        boolean result = emailNotifService.enviarCorreoSiNoEnviado(1, 1, "Recordatorio de pago", "<h1>Recordatorio</h1>");

        Assertions.assertFalse(result);
        verify(emailNotifRepository, never()).save(any(EmailNotif.class));
    }
}
