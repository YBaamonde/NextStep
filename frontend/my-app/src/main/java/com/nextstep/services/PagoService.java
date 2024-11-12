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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PagoService {
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public PagoService() {
        this.baseUrl = "http://localhost:8081"; // URL base del backend
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Obtener todos los pagos de un usuario por su ID
    public List<Map<String, Object>> getPagosPorUsuario(int usuarioId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/pagos/" + usuarioId))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                Notification.show("Error al cargar los pagos: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al cargar los pagos: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    // Crear un nuevo pago
    public Optional<Map<String, Object>> createPago(int usuarioId, Map<String, Object> pago) {
        try {
            String json = objectMapper.writeValueAsString(pago);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/pagos/" + usuarioId))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, Object> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
                return Optional.of(responseMap);
            } else {
                Notification.show("Error al crear el pago: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al crear el pago: " + e.getMessage());
        }
        return Optional.empty();
    }

    // Eliminar un pago
    public boolean deletePago(int pagoId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/pagos/" + pagoId))
                    .header("Authorization", "Bearer " + getToken())
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al eliminar el pago: " + e.getMessage());
        }
        return false;
    }

    // Actualizar un pago existente
    public boolean updatePago(int pagoId, String nombre, double monto, LocalDate fecha, boolean recurrente, String frecuencia) {
        try {
            Map<String, Object> pagoActualizado = Map.of(
                    "nombre", nombre,
                    "monto", monto,
                    "fecha", fecha.toString(),
                    "recurrente", recurrente,
                    "frecuencia", recurrente ? frecuencia : null
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/pagos/" + pagoId))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(pagoActualizado)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al actualizar el pago: " + e.getMessage());
        }
        return false;
    }

    // Obtener todos los pagos recurrentes de un usuario
    public List<Map<String, Object>> getPagosRecurrentesPorUsuario(int usuarioId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/pagos/recurrentes/" + usuarioId))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                Notification.show("Error al cargar los pagos recurrentes: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al cargar los pagos recurrentes: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    // Obtener el token de autenticación
    private String getToken() {
        // Implementación para obtener el token JWT de la sesión o almacenamiento
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}

