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
import java.util.Map;
import java.util.Optional;

public class NotifConfigService {
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public NotifConfigService() {
        this.baseUrl = "http://localhost:8081/notificaciones/config";
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Obtener la configuración del usuario
    public Optional<Map<String, Object>> obtenerConfiguracion(Integer usuarioId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/" + usuarioId))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return Optional.of(objectMapper.readValue(response.body(), new TypeReference<>() {}));
            } else {
                Notification.show("Error al obtener la configuración: " + response.statusCode());
                return Optional.empty();
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al obtener la configuración: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Guardar la configuración del usuario
    public boolean guardarConfiguracion(Map<String, Object> configuracion) {
        try {
            String json = objectMapper.writeValueAsString(configuracion);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Notification.show("Configuración guardada correctamente.");
                return true;
            } else {
                Notification.show("Error al guardar la configuración: " + response.statusCode());
                return false;
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al guardar la configuración: " + e.getMessage());
            return false;
        }
    }

    // Obtener el token de autenticación
    private String getToken() {
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}
