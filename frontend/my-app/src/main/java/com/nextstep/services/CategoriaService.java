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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoriaService {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);

    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public CategoriaService() {
        this.baseUrl = "http://localhost:8081";
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Obtener todas las categorías de un usuario por su ID
    public List<Map<String, Object>> getCategoriasPorUsuario(int usuarioId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/categorias/" + usuarioId))
                    .header("Authorization", "Bearer " + getToken()) // Asegúrate de que el token se envía
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                Notification.show("Error al cargar las categorías: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al cargar las categorías: " + e.getMessage());
        }

        return Collections.emptyList();
    }


    // Crear una nueva categoría
    public Optional<Map<String, Object>> createCategoria(int usuarioId, Map<String, Object> categoria) {
        try {
            String json = objectMapper.writeValueAsString(categoria);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/categorias/" + usuarioId))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parse the response JSON into a Map and return it
                Map<String, Object> createdCategoria = objectMapper.readValue(response.body(), new TypeReference<>() {});
                return Optional.of(createdCategoria);
            } else {
                Notification.show("Error al crear la categoría: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al crear la categoría: " + e.getMessage());
        }
        return Optional.empty();
    }


    // Eliminar una categoría
    public boolean deleteCategoria(int categoriaId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/categorias/" + categoriaId))
                    .header("Authorization", "Bearer " + getToken())
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println("Delete Categoria Response Code: " + response.statusCode()); // Debug
            logger.info("Delete Categoria Response Code: " + response.statusCode());

            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al eliminar la categoría: " + e.getMessage());
        }
        return false;
    }

    // Actualizar o editar categoría
    public boolean updateCategoria(int categoriaId, String nuevoNombre, String nuevaDescripcion) {
        try {
            Map<String, Object> categoriaActualizada = Map.of(
                    "nombre", nuevoNombre,
                    "descripcion", nuevaDescripcion
            );
            String json = objectMapper.writeValueAsString(categoriaActualizada);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/categorias/" + categoriaId))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println("Update Categoria Response Code: " + response.statusCode()); // Debug
            //System.out.println("Update Categoria Response Body: " + response.body()); // Debug

            if (response.statusCode() == 200) {
                Notification.show("Categoría actualizada con éxito");
                return true;
            } else {
                Notification.show("Error al actualizar la categoría: " + response.statusCode());
                return false;
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al actualizar la categoría: " + e.getMessage());
            return false;
        }
    }






    // Obtener el token de autenticación
    private String getToken() {
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}
