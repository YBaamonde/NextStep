package com.nextstep.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextstep.views.helloworld.HelloWorldView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Router;
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

    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    // private final Router router; // Inyecta el router para manejar redirecciones

    public AuthService(/* Router router */) {
        this.baseUrl = "http://localhost:8081";
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        // this.router = router; // Inicializa el router
    }

    public void login(String username, String password) {
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", username);
            requestBody.put("password", password);

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, String> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
                String token = responseMap.get("token");
                Notification.show("Inicio de sesión exitoso. Token: " + token);

                // Redirige a la página de inicio después del inicio de sesión exitoso
                UI.getCurrent().navigate(HelloWorldView.class); // Navegar a HelloWorldView
            } else {
                Notification.show("Error en el inicio de sesión. Estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Ocurrió un error durante el inicio de sesión: " + e.getMessage());
        }
    }

    public void register(String username, String email, String password, String confirmPassword) {
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("nombre", username); // 'nombre' en lugar de 'username'
            requestBody.put("username", email); // 'username' como el email
            requestBody.put("password", password);
            requestBody.put("rol", "normal"); // Asignar "normal" directamente

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/auth/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                Notification.show("Registro exitoso. Redirigiendo a la página de inicio de sesión...");

                // Redirige a la página de inicio de sesión después del registro exitoso
                UI.getCurrent().navigate("login");  // Navegar a la vista de login
            } else {
                Notification.show("Error en el registro. Estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Ocurrió un error durante el registro: " + e.getMessage());
        }
    }

}
