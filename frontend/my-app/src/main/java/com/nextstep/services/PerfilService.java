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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PerfilService {
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public PerfilService() {
        //this.baseUrl = "http://localhost:8081"; // URL base del backend
        this.baseUrl = "http://backend:8081"; // Nombre del servicio 'backend' y puerto del backend
        this.client = HttpClient.newHttpClient(); // Cliente HTTP para peticiones
        this.objectMapper = new ObjectMapper(); // Mapeador JSON
    }

    // Obtener detalles del perfil
    public Optional<Map<String, Object>> getPerfil(int usuarioId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/perfil/" + usuarioId))
                    .header("Authorization", "Bearer " + getToken()) // Token de autenticación
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return Optional.of(objectMapper.readValue(response.body(), new TypeReference<>() {}));
            } else {
                Notification.show("Error al obtener el perfil: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al obtener el perfil: " + e.getMessage());
        }
        return Optional.empty();
    }

    // Actualizar contraseña
    public boolean updatePassword(int usuarioId, String newPassword) {
        try {
            Map<String, String> requestMap = Map.of("newPassword", newPassword);
            String json = objectMapper.writeValueAsString(requestMap);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/perfil/" + usuarioId + "/password"))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, String> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
                Notification.show("Contraseña actualizada con éxito.");
                return true;
            } else {
                Notification.show("Error al actualizar la contraseña: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al actualizar la contraseña: " + e.getMessage());
        }
        return false;
    }


    // Eliminar cuenta
    public boolean deleteUsuario(int usuarioId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/perfil/" + usuarioId))
                    .header("Authorization", "Bearer " + getToken())
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Notification.show("Cuenta eliminada con éxito.");
                return true;
            } else {
                Notification.show("Error al eliminar la cuenta: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al eliminar la cuenta: " + e.getMessage());
        }
        return false;
    }


    // Actualizar el nombre de usuario
    public boolean updateUsername(int usuarioId, String newUsername) {
        try {
            // Crear el cuerpo de la solicitud en formato JSON
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("newUsername", newUsername);
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            // Configurar la solicitud PUT para actualizar el nombre de usuario
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/perfil/" + usuarioId + "/username"))
                    .header("Authorization", "Bearer " + getToken()) // Agregar token de autenticación
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            // Enviar la solicitud y manejar la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parsear la respuesta JSON para obtener el nuevo token
                Map<String, String> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
                String newToken = responseMap.get("token");

                // Actualizar el token en la sesión
                UI.getCurrent().getSession().setAttribute("authToken", newToken);

                // Notificar éxito
                Notification.show("Nombre de usuario actualizado correctamente.");
                return true;
            } else {
                Notification.show("Error al actualizar el nombre de usuario: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al actualizar el nombre de usuario: " + e.getMessage());
        }
        return false;
    }


    // Obtener token de autenticación de la sesión
    private String getToken() {
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}

