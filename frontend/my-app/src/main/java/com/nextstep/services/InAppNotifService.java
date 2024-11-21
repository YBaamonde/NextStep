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
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InAppNotifService {
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public InAppNotifService() {
        this.baseUrl = "http://localhost:8081"; // URL base del backend
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Crear una nueva notificación In-App
    public boolean crearNotificacion(Integer usuarioId, Integer pagoId, String titulo, String mensaje) {
        try {
            Map<String, String> notificacionData = Map.of(
                    "titulo", titulo,
                    "mensaje", mensaje
            );

            String json = objectMapper.writeValueAsString(notificacionData);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/notificaciones/inapp?usuarioId=" + usuarioId + "&pagoId=" + pagoId))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Notification.show("Notificación creada correctamente.");
                return true;
            } else {
                Notification.show("Error al crear la notificación: " + response.statusCode());
                return false;
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al crear la notificación: " + e.getMessage());
            return false;
        }
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
    public boolean marcarComoLeida(Integer notificacionId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/notificaciones/inapp/" + notificacionId + "/leida"))
                    .header("Authorization", "Bearer " + getToken())
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Notification.show("Notificación marcada como leída.");
                return true;
            } else {
                Notification.show("Error al marcar como leída: " + response.statusCode());
                return false;
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al marcar como leída: " + e.getMessage());
            return false;
        }
    }

    // Eliminar una notificación
    public boolean eliminarNotificacion(Integer notificacionId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/notificaciones/inapp/" + notificacionId))
                    .header("Authorization", "Bearer " + getToken())
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Notification.show("Notificación eliminada correctamente.");
                return true;
            } else {
                Notification.show("Error al eliminar la notificación: " + response.statusCode());
                return false;
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al eliminar la notificación: " + e.getMessage());
            return false;
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

    // Obtener el token de autenticación
    private String getToken() {
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}

