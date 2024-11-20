package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nextstep.nextstepBackEnd.controller.notif.NotificacionRequest;
import com.nextstep.nextstepBackEnd.model.Notificacion;
import com.nextstep.nextstepBackEnd.model.NotificacionDTO;
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
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InAppNotifControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InAppNotifService inAppNotifService;

    @Autowired
    private ObjectMapper objectMapper;

    private Notificacion notificacion;
    private NotificacionDTO notificacionDTO;

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

        notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setUsuario(usuario);
        notificacion.setPago(pago);
        notificacion.setTitulo("Recordatorio");
        notificacion.setMensaje("Pago programado para mañana.");
        notificacion.setLeido(false);
        notificacion.setFechaCreacion(LocalDateTime.now());

        notificacionDTO = new NotificacionDTO();
        notificacionDTO.setId(1);
        notificacionDTO.setTitulo("Recordatorio");
        notificacionDTO.setMensaje("Pago programado para mañana.");
        notificacionDTO.setLeido(false);
        notificacionDTO.setFechaCreacion(notificacion.getFechaCreacion());
        notificacionDTO.setPagoId(pago.getId());
    }



    @Test
    @WithMockUser
    public void testCrearNotificacion() throws Exception {
        when(inAppNotifService.crearNotificacion(eq(1), eq(1), anyString(), anyString()))
                .thenReturn(notificacion);
        when(inAppNotifService.convertirADTO(any(Notificacion.class)))
                .thenReturn(notificacionDTO);

        NotificacionRequest request = new NotificacionRequest();
        request.setTitulo("Recordatorio");
        request.setMensaje("Pago programado para mañana.");

        mockMvc.perform(post("/notificaciones/inapp?usuarioId=1&pagoId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Recordatorio"))
                .andExpect(jsonPath("$.mensaje").value("Pago programado para mañana."));
    }




    @Test
    @WithMockUser
    public void testObtenerNotificaciones() throws Exception {
        when(inAppNotifService.obtenerNotificacionesPorUsuario(1))
                .thenReturn(List.of(notificacion));
        when(inAppNotifService.convertirADTO(any(Notificacion.class)))
                .thenReturn(notificacionDTO);

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
        // Configura el mock para simular el servicio
        when(inAppNotifService.marcarComoLeida(1)).thenAnswer(invocation -> {
            notificacion.setLeido(true);
            notificacion.setFechaLeido(LocalDateTime.now());
            return notificacion;
        });

        // Configura el mock para convertir a DTO
        when(inAppNotifService.convertirADTO(any(Notificacion.class)))
                .thenAnswer(invocation -> {
                    Notificacion n = invocation.getArgument(0);
                    NotificacionDTO dto = new NotificacionDTO();
                    dto.setId(n.getId());
                    dto.setTitulo(n.getTitulo());
                    dto.setMensaje(n.getMensaje());
                    dto.setLeido(n.getLeido());
                    dto.setFechaCreacion(n.getFechaCreacion());
                    dto.setFechaLeido(n.getFechaLeido());
                    dto.setPagoId(n.getPago().getId());
                    return dto;
                });

        // Ejecuta la solicitud PUT
        mockMvc.perform(put("/notificaciones/inapp/1/leida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.leido").value(true))
                .andExpect(jsonPath("$.titulo").value("Recordatorio"));
    }




    @Test
    @WithMockUser
    public void testEliminarNotificacion() throws Exception {
        doNothing().when(inAppNotifService).eliminarNotificacion(1);

        mockMvc.perform(delete("/notificaciones/inapp/1"))
                .andExpect(status().isOk());

        verify(inAppNotifService, times(1)).eliminarNotificacion(1);
    }

}

