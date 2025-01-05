package com.nextstep.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InAppNotifService {
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public InAppNotifService() {
        //this.baseUrl = "http://localhost:8081"; // URL base del backend
        this.baseUrl = "http://backend:8081"; // Nombre del servicio 'backend' y puerto del backend
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Obtener todas las notificaciones de un usuario
    public List<Map<String, Object>> obtenerNotificaciones(Integer usuarioId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/notificaciones/inapp/" + usuarioId))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                Notification.show("Error al obtener notificaciones: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al obtener notificaciones: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Marcar una notificación como leída
    public void marcarComoLeida(Integer notificacionId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/notificaciones/inapp/" + notificacionId + "/leida"))
                    .header("Authorization", "Bearer " + getToken())
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Notification.show("Notificación marcada como leída.");
            } else {
                Notification.show("Error al marcar como leída: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al marcar como leída: " + e.getMessage());
        }
    }

    // Contar las notificaciones no leídas
    public int contarNotificacionesNoLeidas(Integer usuarioId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/notificaciones/inapp/" + usuarioId + "/no-leidas"))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return Integer.parseInt(response.body());
            } else {
                Notification.show("Error al contar notificaciones: " + response.statusCode());
                return 0;
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al contar notificaciones: " + e.getMessage());
            return 0;
        }
    }

    // Filtrar notificaciones expiradas
    public List<Map<String, Object>> filtrarNotificacionesExpiradas(List<Map<String, Object>> notificaciones) {
        LocalDateTime ahora = LocalDateTime.now();

        // Filtra notificaciones cuya fecha de lectura supere los 30 segundos para pruebas
        return notificaciones.stream()
                .filter(notificacion -> {
                    if ((boolean) notificacion.get("leido")) {
                        LocalDateTime fechaLeido = LocalDateTime.parse((String) notificacion.get("fechaLeido"));
                        return fechaLeido.plusSeconds(30).isAfter(ahora); // Para pruebas, 30 segundos
                        // return fechaLeido.plusHours(5).isAfter(ahora); // Para producción, 5 horas
                    }
                    return true; // Si no está leída, mantener la notificación
                })
                .collect(Collectors.toList());
    }


    // Obtener el token de autenticación
    private String getToken() {
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}

