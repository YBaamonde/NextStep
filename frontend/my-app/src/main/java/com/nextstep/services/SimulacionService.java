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

public class SimulacionService {
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public SimulacionService() {
        this.baseUrl = "http://localhost:8081"; // URL base del backend
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Metodo para enviar datos de simulación y calcular resultados
    public Optional<Map<String, Object>> calcularSimulacion(Map<String, Object> simulacionData) {
        try {
            String json = objectMapper.writeValueAsString(simulacionData);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/simulacion/calcular"))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Convertir la respuesta JSON en un Map
                Map<String, Object> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
                return Optional.of(responseMap);
            } else {
                Notification.show("Error al calcular la simulación: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al calcular la simulación: " + e.getMessage());
        }
        return Optional.empty();
    }

    // Obtener el token de autenticación
    private String getToken() {
        // Implementación para obtener el token JWT de la sesión o almacenamiento
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}

