package com.nextstep.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimulacionService {
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public SimulacionService() {
        this.baseUrl = "http://localhost:8081"; // URL base del backend
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Metodo para enviar datos de simulación y calcular resultados
    public Optional<Map<String, Object>> calcularSimulacion(Map<String, Object> simulacionData) {
        try {
            // Validar tipos de datos de gastos
            if (!(simulacionData.get("gastosEsenciales") instanceof Map) || !(simulacionData.get("gastosOpcionales") instanceof Map)) {
                Notification.show("Error en los datos: los gastos no tienen el formato esperado.");
                return Optional.empty();
            }

            // Estructura los gastos clasificados en un solo campo
            Map<String, Map<String, Double>> gastosClasificados = new HashMap<>();
            gastosClasificados.put("esenciales", (Map<String, Double>) simulacionData.get("gastosEsenciales"));
            gastosClasificados.put("opcionales", (Map<String, Double>) simulacionData.get("gastosOpcionales"));

            // Elimina los campos individuales de gastos y agrega `gastosClasificados`
            simulacionData.remove("gastosEsenciales");
            simulacionData.remove("gastosOpcionales");
            simulacionData.put("gastosClasificados", gastosClasificados);

            // Validar campos necesarios
            if (!simulacionData.containsKey("metaAhorro") || !simulacionData.containsKey("mesesSimulacion") || !simulacionData.containsKey("ingresos")) {
                Notification.show("Faltan campos requeridos en la simulación.");
                return Optional.empty();
            }

            // Serializa el JSON y envía la solicitud como antes
            String json = objectMapper.writeValueAsString(simulacionData);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/simulacion/calcular"))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            // Procesar la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Map<String, Object> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
                return Optional.of(responseMap);
            } else {
                Notification.show("Error al calcular la simulación: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al calcular la simulación: " + e.getMessage());
        }
        return Optional.empty();
    }



    // Metodo para exportar la simulación como PDF
    public void exportarSimulacionPdf(Map<String, Object> simulacionData) {
        try {
            if (simulacionData == null || simulacionData.isEmpty()) {
                Notification.show("Error: Los datos de simulación son inválidos o están vacíos.");
                return;
            }

            String json = objectMapper.writeValueAsString(simulacionData); // Serializa a JSON
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/simulacion/exportar"))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json)) // Enviar JSON
                    .build();

            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                byte[] pdfBytes = response.body();

                // Crear un StreamResource para encapsular el PDF
                StreamResource pdfResource = new StreamResource("simulacion.pdf", () -> new ByteArrayInputStream(pdfBytes));
                pdfResource.setContentType("application/pdf");
                pdfResource.setCacheTime(0); // Evitar cacheo

                // Registrar el recurso en el ResourceRegistry
                StreamRegistration registration = UI.getCurrent().getSession().getResourceRegistry().registerResource(pdfResource);

                // Obtener la URL del recurso
                String pdfUrl = registration.getResourceUri().toString();

                // Abrir el archivo PDF en una nueva ventana del navegador
                UI.getCurrent().getPage().open(pdfUrl, "_blank");
            } else {
                Notification.show("Error al exportar el PDF: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al exportar el PDF: " + e.getMessage());
        }
    }


    // Obtener el token de autenticación
    private String getToken() {
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}
