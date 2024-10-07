package com.nextstep.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final String baseUrl; // URL base del backend
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    // Constructor que acepta la URL base
    public AuthService() {
        this.baseUrl = "http://localhost:8080";
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Método para realizar login
    public void login(String email, String password) {
        try {
            // Crear el cuerpo del request
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", email);
            requestBody.put("password", password);

            // Convertir el cuerpo a JSON
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            // Crear la solicitud HTTP POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/auth/login"))  // Endpoint de login
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            // Enviar la solicitud
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar si la respuesta es exitosa (código 200)
            if (response.statusCode() == 200) {
                // Aquí se maneja el JWT recibido
                Map<String, String> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
                String token = responseMap.get("token");

                Notification.show("Inicio de sesión exitoso. Token: " + token);
                // Almacena el token o úsalo para futuras solicitudes
            } else {
                Notification.show("Error en el inicio de sesión. Estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Ocurrió un error durante el inicio de sesión: " + e.getMessage());
        }
    }

    // Método para registrar al usuario (sin el parámetro 'rol')
    public void register(String username, String email, String password, String confirmPassword) {
        try {
            // Crear el cuerpo del request
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("nombre", username);  // Se espera el 'nombre' del usuario
            requestBody.put("username", email);
            requestBody.put("password", password);
            requestBody.put("confirmPassword", confirmPassword);  // Validar las contraseñas antes de enviar

            // Convertir el cuerpo a JSON
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            // Crear la solicitud HTTP POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/auth/register"))  // Endpoint de registro
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            // Enviar la solicitud
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar si la respuesta es exitosa (código 201 para creación exitosa)
            if (response.statusCode() == 201) {
                Notification.show("Registro exitoso.");
            } else {
                Notification.show("Error en el registro. Estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Ocurrió un error durante el registro: " + e.getMessage());
        }
    }
}
