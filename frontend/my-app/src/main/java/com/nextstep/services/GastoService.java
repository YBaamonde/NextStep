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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GastoService {
    private static final Logger logger = LoggerFactory.getLogger(GastoService.class);

    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public GastoService() {
        this.baseUrl = "http://localhost:8081"; // URL base del backend
        this.client = HttpClient.newHttpClient(); // Cliente HTTP para hacer peticiones
        this.objectMapper = new ObjectMapper(); // Mapeador JSON
    }

    // Obtener todos los gastos de un usuario por categoría con un límite
    public List<Map<String, Object>> getGastosPorCategoriaConLimite(int categoriaId, int limite) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/gastos/categoria/" + categoriaId + "?limite=" + limite))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                Notification.show("Error al cargar los gastos: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al cargar los gastos: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    // Crear un nuevo gasto
    public Optional<Map<String, Object>> createGasto(int usuarioId, int categoriaId, Map<String, Object> gasto) {
        try {
            String json = objectMapper.writeValueAsString(gasto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/gastos/" + usuarioId + "/" + categoriaId))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, Object> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
                return Optional.of(responseMap);
            } else {
                Notification.show("Error al crear el gasto: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al crear el gasto: " + e.getMessage());
        }
        return Optional.empty();
    }


    // Eliminar un gasto
    public boolean deleteGasto(int gastoId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/gastos/" + gastoId))
                    .header("Authorization", "Bearer " + getToken())
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al eliminar el gasto: " + e.getMessage());
        }
        return false;
    }

    // Metodo para actualizar un gasto existente
    public boolean updateGasto(int gastoId, String nombre, double monto, LocalDate fecha) {
        try {
            Map<String, Object> gastoActualizado = Map.of(
                    "nombre", nombre,
                    "monto", monto,
                    "fecha", fecha.toString()
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/gastos/" + gastoId))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(gastoActualizado)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println("Update Gasto Response Code: " + response.statusCode());
            //System.out.println("Update Gasto Response Body: " + response.body());
            logger.info("Update Gasto Response Code: " + response.statusCode());
            logger.info("Update Gasto Response Body: " + response.body());

            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al actualizar el gasto: " + e.getMessage());
        }
        return false;
    }


    public List<Map<String, Object>> getGastosHistoricosPorCategoria(int categoriaId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/gastos/categoria/" + categoriaId + "/historicos"))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                Notification.show("Error al cargar los gastos históricos: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al cargar los gastos históricos: " + e.getMessage());
        }
        return Collections.emptyList();
    }






    // Metodo para obtener el token de autenticación
    private String getToken() {
        // Implementación para obtener el token JWT de la sesión o almacenamiento
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }

}
