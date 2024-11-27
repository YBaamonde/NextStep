package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextstep.nextstepBackEnd.service.notif.EmailNotifService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailNotifControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailNotifService emailNotifService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testEnviarEmailHtml() throws Exception {
        doNothing().when(emailNotifService).enviarEmailHtml("testuser@example.com", "Asunto", "<h1>Mensaje</h1>");

        mockMvc.perform(post("/notificaciones/email")
                        .param("destinatario", "testuser@example.com")
                        .param("asunto", "Asunto")
                        .param("mensajeHtml", "<h1>Mensaje</h1>"))
                .andExpect(status().isOk())
                .andExpect(content().string("Correo HTML enviado correctamente a testuser@example.com"));
    }

    @Test
    @WithMockUser
    void testEnviarEmailHtml_Error() throws Exception {
        doThrow(new MessagingException("Error de envío")).when(emailNotifService).enviarEmailHtml("testuser@example.com", "Asunto", "<h1>Mensaje</h1>");

        mockMvc.perform(post("/notificaciones/email")
                        .param("destinatario", "testuser@example.com")
                        .param("asunto", "Asunto")
                        .param("mensajeHtml", "<h1>Mensaje</h1>"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al enviar el correo: Error de envío"));
    }

    @Test
    @WithMockUser
    void testVerificarCorreoEnviado() throws Exception {
        when(emailNotifService.enviarCorreoSiNoEnviado(1, 1, "Recordatorio de pago", "2024-11-28")).thenReturn(true);

        mockMvc.perform(get("/notificaciones/email/enviado")
                        .param("usuarioId", "1")
                        .param("pagoId", "1")
                        .param("fechaPago", "2024-11-28"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
