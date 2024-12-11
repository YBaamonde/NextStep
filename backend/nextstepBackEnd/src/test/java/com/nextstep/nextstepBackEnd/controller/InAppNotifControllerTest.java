package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextstep.nextstepBackEnd.controller.notif.NotificacionRequest;
import com.nextstep.nextstepBackEnd.model.notif.InAppNotif;
import com.nextstep.nextstepBackEnd.model.notif.InAppNotifDTO;
import com.nextstep.nextstepBackEnd.model.Pago;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.service.notif.InAppNotifService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class InAppNotifControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InAppNotifService inAppNotifService;

    @Autowired
    private ObjectMapper objectMapper;

    private InAppNotif inAppNotif;
    private InAppNotifDTO inAppNotifDTO;

    @BeforeEach
    void setUp() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("testuser");

        Pago pago = new Pago();
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

        inAppNotifDTO = new InAppNotifDTO();
        inAppNotifDTO.setId(1);
        inAppNotifDTO.setTitulo("Recordatorio");
        inAppNotifDTO.setMensaje("Pago programado para mañana.");
        inAppNotifDTO.setLeido(false);
        inAppNotifDTO.setFechaCreacion(inAppNotif.getFechaCreacion());
        inAppNotifDTO.setPagoId(pago.getId());
    }

    @Test
    @WithMockUser
    public void testCrearNotificacionConFechaPago() throws Exception {
        // Mockear la respuesta del servicio
        when(inAppNotifService.crearNotificacion(eq(1), eq(1), anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(inAppNotif);
        when(inAppNotifService.convertirADTO(any(InAppNotif.class)))
                .thenReturn(inAppNotifDTO);

        // Crear la solicitud
        NotificacionRequest request = new NotificacionRequest();
        request.setTitulo("Recordatorio");
        request.setMensaje("Pago programado para mañana.");

        mockMvc.perform(post("/notificaciones/inapp?usuarioId=1&pagoId=1&fechaPago=2024-11-28")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Recordatorio"))
                .andExpect(jsonPath("$.mensaje").value("Pago programado para mañana."))
                .andExpect(jsonPath("$.fechaCreacion").exists());
    }

    @Test
    @WithMockUser
    public void testObtenerNotificaciones() throws Exception {
        when(inAppNotifService.obtenerNotificacionesPorUsuario(1))
                .thenReturn(List.of(inAppNotif));
        when(inAppNotifService.convertirADTO(any(InAppNotif.class)))
                .thenReturn(inAppNotifDTO);

        mockMvc.perform(get("/notificaciones/inapp/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Recordatorio"))
                .andExpect(jsonPath("$[0].mensaje").value("Pago programado para mañana."));
    }

    @Test
    @WithMockUser
    public void testContarNotificacionesNoLeidas() throws Exception {
        when(inAppNotifService.contarNotificacionesNoLeidas(1))
                .thenReturn(5L);

        mockMvc.perform(get("/notificaciones/inapp/1/no-leidas"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    @WithMockUser
    public void testMarcarComoLeida() throws Exception {
        // Mockear respuesta del servicio
        when(inAppNotifService.marcarComoLeida(1)).thenAnswer(invocation -> {
            inAppNotif.setLeido(true);
            inAppNotif.setFechaLeido(LocalDateTime.now());
            return inAppNotif;
        });

        // Mockear conversión a DTO
        when(inAppNotifService.convertirADTO(any(InAppNotif.class)))
                .thenAnswer(invocation -> {
                    InAppNotif n = invocation.getArgument(0);
                    InAppNotifDTO dto = new InAppNotifDTO();
                    dto.setId(n.getId());
                    dto.setTitulo(n.getTitulo());
                    dto.setMensaje(n.getMensaje());
                    dto.setLeido(n.getLeido());
                    dto.setFechaCreacion(n.getFechaCreacion());
                    dto.setFechaLeido(n.getFechaLeido());
                    dto.setPagoId(n.getPago().getId());
                    return dto;
                });

        // Ejecutar solicitud PUT
        mockMvc.perform(put("/notificaciones/inapp/1/leida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.leido").value(true))
                .andExpect(jsonPath("$.titulo").value("Recordatorio"));
    }
}
