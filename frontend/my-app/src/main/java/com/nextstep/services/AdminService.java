package com.nextstep.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final String baseUrl = "http://localhost:8081"; // URL del backend
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public AdminService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Metodo para obtener todos los usuarios
    public List<Map<String, Object>> getUsers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/admin/users"))
                    .header("Authorization", "Bearer " + getAuthToken())  // Añadir el token de autorización
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                Notification.show("Error obteniendo usuarios. Estado: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Ocurrió un error: " + e.getMessage());
            return null;
        }
    }

    // Metodo para eliminar un usuario por su ID
    public void deleteUser(Long userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/admin/delete-user/" + userId))
                    .header("Authorization", "Bearer " + getAuthToken())  // Añadir el token de autorización
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Notification.show("Usuario eliminado exitosamente.");
            } else {
                Notification.show("Error eliminando usuario. Estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Ocurrió un error: " + e.getMessage());
        }
    }

    // Metodo para crear un nuevo usuario
    public void createUser(String username, String email, String password, String rol) {
        try {
            Map<String, String> requestBody = Map.of(
                    "username", username,
                    "email", email,
                    "password", password,
                    "rol", rol
            );

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/admin/create-user"))
                    .header("Authorization", "Bearer " + getAuthToken())  // Añadir el token de autorización
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Notification.show("Usuario creado exitosamente.");
            } else {
                Notification.show("Error creando usuario. Estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Ocurrió un error: " + e.getMessage());
        }
    }

    // Metodo auxiliar para obtener el token de autorización (este puede cambiar según cómo lo gestiones)
    private String getAuthToken() {
        // Suponiendo que has guardado el token en la sesión, podrías obtenerlo de allí
        // Por ejemplo, podrías guardarlo como un atributo de sesión en el login
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}
