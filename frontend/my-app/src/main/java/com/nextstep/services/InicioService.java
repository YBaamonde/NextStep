package com.nextstep.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

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
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/inicio/" + usuarioId))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, Object> data = objectMapper.readValue(response.body(), new TypeReference<>() {});
                return Optional.of(data);
            } else {
                System.err.println("Error al obtener los datos de inicio: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al obtener los datos de inicio: " + e.getMessage());
        }
        return Optional.empty();
    }

    private String getToken() {
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}