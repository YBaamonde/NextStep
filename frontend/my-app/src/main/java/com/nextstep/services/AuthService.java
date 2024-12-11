package com.nextstep.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
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
                Map<String, Object> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
                String token = (String) responseMap.get("token");
                Integer userId = (Integer) responseMap.get("userId"); // Obtener el userId de la respuesta

                // Almacenar el token en la sesión
                UI.getCurrent().getSession().setAttribute("authToken", token);

                // Almacenar el userId en la sesión
                UI.getCurrent().getSession().setAttribute("userId", userId);

                // Almacenar el nombre de usuario en la sesión
                UI.getCurrent().getSession().setAttribute("username", username);

                loginCallback.accept(true);
            } else {
                loginCallback.accept(false);
            }
        } catch (IOException | InterruptedException e) {
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



    // Métodos para recopilar info de Usuarios desde el back

    public List<Map<String, Object>> getAllUsers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/admin/users"))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Deserializa la respuesta a una lista de mapas
                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> users = mapper.readValue(response.body(), new TypeReference<>() {});

                // Devuelve la lista de usuarios deserializada
                return users;
            } else {
                Notification.show("Error al cargar los usuarios: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al cargar los usuarios: " + e.getMessage());
        }

        return Collections.emptyList();
    }


    public boolean deleteUser(Long userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/admin/delete-user/" + userId))
                    .header("Authorization", "Bearer " + getToken())
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean createUser(String username, String email, String password, String role) {
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", username);
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("rol", role);

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/admin/create-user"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    // Metodo auxiliar para obtener el token desde la sesión
    private String getToken() {
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }

    // Metodo para obtener el nombre de usuario desde la sesión
    public String getUsername() {
        return (String) UI.getCurrent().getSession().getAttribute("username");
    }

    // Metodo para obtener el Id de un usuario desde la sesión
    public Integer getUserId(){
        return (Integer) VaadinSession.getCurrent().getAttribute("userId");
    }


    // Metodo para comprobar la contraseña (Para actualizarla)
    public void validatePassword(String username, String password, Consumer<Boolean> callback) {
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", username);
            requestBody.put("password", password);

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/auth/login"))  // Usamos la misma ruta de autenticación
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Si la respuesta es 200, significa que la contraseña es válida
            callback.accept(response.statusCode() == 200);

        } catch (IOException | InterruptedException e) {
            callback.accept(false);
        }
    }
}
