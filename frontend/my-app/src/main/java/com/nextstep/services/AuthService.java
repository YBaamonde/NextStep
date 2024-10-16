package com.nextstep.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextstep.views.helloworld.HelloWorldView;
import com.vaadin.base.devserver.DevToolsInterface;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class AuthService {

    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    // Constructor para inicializar el HttpClient y el ObjectMapper, además de la URL base del backend.
    public AuthService() {
        this.baseUrl = "http://localhost:8081"; // Establece la URL base para todas las peticiones al backend
        this.client = HttpClient.newHttpClient(); // Crea un nuevo cliente HTTP para manejar las peticiones
        this.objectMapper = new ObjectMapper(); // Se utiliza para convertir objetos Java a JSON y viceversa
    }

    // Metodo para iniciar sesión, enviando las credenciales al backend y gestionando la respuesta
    public void login(String username, String password, Consumer<Boolean> loginCallback) {
        System.out.println("Iniciando petición de login");

        try {
            // Crea un mapa con las credenciales que se enviarán en la solicitud HTTP
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", username);
            requestBody.put("password", password);

            // Convierte el mapa de credenciales a formato JSON
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            // Construye la solicitud HTTP para el login
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/auth/login")) // Establece la URL de login en el backend
                    .header("Content-Type", "application/json") // Establece el tipo de contenido de la solicitud como JSON
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson)) // Agrega el cuerpo de la solicitud con las credenciales en formato JSON
                    .build();

            // Envía la solicitud al servidor y obtiene la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Si el código de estado HTTP es 200, significa que el login fue exitoso
            if (response.statusCode() == 200) {
                // Convierte la respuesta JSON a un mapa y extrae el token JWT
                Map<String, String> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
                String token = responseMap.get("token");

                // Almacenar el token en la sesión de Vaadin
                UI.getCurrent().getSession().setAttribute("authToken", token);

                // Ejecuta el callback indicando éxito (true)
                loginCallback.accept(true);
            } else {
                // Si el código de estado es diferente a 200, ejecuta el callback indicando fallo (false)
                loginCallback.accept(false);
            }
        } catch (IOException | InterruptedException e) {
            // En caso de error, ejecuta el callback indicando fallo (false)
            loginCallback.accept(false);
        }
    }




    // Metodo para registrar un nuevo usuario enviando los datos al backend
    public void register(String username, String email, String password, String confirmPassword) {
        try {
            // Crea un mapa con los datos de registro del usuario
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", username);
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("rol", "normal"); // Asigna el rol "normal" por defecto

            // Convierte los datos del usuario a formato JSON
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            // Construye la solicitud HTTP para el registro
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/auth/register")) // Establece la URL de registro en el backend
                    .header("Content-Type", "application/json") // Establece el tipo de contenido de la solicitud como JSON
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson)) // Agrega el cuerpo de la solicitud con los datos del usuario en formato JSON
                    .build();

            // Envía la solicitud al servidor y obtiene la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Si el código de estado HTTP es 201, el registro fue exitoso
            if (response.statusCode() == 201) {
                // Muestra una notificación y redirige al usuario a la página de inicio de sesión
                Notification.show("Registro exitoso. Redirigiendo a la página de inicio de sesión...");
                UI.getCurrent().navigate("login"); // Redirige a la vista de login
            } else {
                // Si el código de estado es diferente a 201, muestra un mensaje de error con el estado devuelto
                Notification.show("Error en el registro. Estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            // Captura y muestra cualquier error que ocurra durante el proceso de registro
            Notification.show("Ocurrió un error durante el registro: " + e.getMessage());
        }
    }

    // Metodo para crear solicitudes autenticadas con el token JWT
    public HttpRequest createAuthenticatedRequest(String endpoint) {
        // Recupera el token JWT de la sesión
        String token = (String) UI.getCurrent().getSession().getAttribute("authToken");

        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .header("Authorization", "Bearer " + token) // Envía el token en el header de autorización
                .build();
    }


    // Metodo para obtener el HttpClient
    public HttpClient getClient() {
        return this.client;
    }

}
