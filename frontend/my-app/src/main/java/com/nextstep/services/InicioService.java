package com.nextstep.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class InicioService {
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public InicioService() {
        this.baseUrl = "http://localhost:8081"; // URL base del backend
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Metodo para obtener los datos de inicio de un usuario
    public Optional<Map<String, Object>> getInicioData(int usuarioId) {
        Logger logger = Logger.getLogger(getClass().getName()); // Configurar un logger para mensajes

        // Verificar entrada
        if (usuarioId <= 0) {
            logger.warning("ID de usuario inválido: " + usuarioId);
            return Optional.empty();
        }

        try {
            // Construir la solicitud HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/inicio/" + usuarioId))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            // Enviar la solicitud y manejar la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Manejar respuesta exitosa
            if (response.statusCode() == 200) {
                String responseBody = response.body();

                // Validar que el cuerpo no esté vacío
                if (responseBody == null || responseBody.isBlank()) {
                    logger.warning("El cuerpo de la respuesta está vacío para el usuario ID: " + usuarioId);
                    return Optional.empty();
                }

                // Deserializar la respuesta a un mapa
                Map<String, Object> data = objectMapper.readValue(responseBody, new TypeReference<>() {});
                return Optional.of(data);
            } else {
                // Log para errores específicos de HTTP
                logger.warning("Error al obtener datos de inicio para el usuario ID: " + usuarioId +
                        ". Código de estado: " + response.statusCode());
            }
        } catch (IOException e) {
            logger.severe("Error de entrada/salida al obtener datos de inicio para el usuario ID: " + usuarioId +
                    ". Mensaje: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.severe("Solicitud interrumpida al obtener datos de inicio para el usuario ID: " + usuarioId);
            Thread.currentThread().interrupt(); // Restaurar el estado interrumpido del hilo
        } catch (Exception e) {
            logger.severe("Error inesperado al obtener datos de inicio para el usuario ID: " + usuarioId +
                    ". Mensaje: " + e.getMessage());
        }

        // En caso de error, devolver un Optional vacío
        return Optional.empty();
    }

    private String getToken() {
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}