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
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulacionService {
    private static final Logger logger = LoggerFactory.getLogger(SimulacionService.class);

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
        //System.out.println("Datos enviados al backend para la simulación: " + simulacionData); // Debug
        logger.info("Datos enviados al backend para la simulación: " + simulacionData);
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

            simulacionData.remove("gastosEsenciales");
            simulacionData.remove("gastosOpcionales");
            simulacionData.put("gastosClasificados", gastosClasificados);

            //System.out.println("Datos estructurados para la simulación: " + simulacionData); // Debug
            logger.info("Datos estructurados para la simulación: " + simulacionData);

            // Serializa el JSON y envía la solicitud
            String json = objectMapper.writeValueAsString(simulacionData);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/simulacion/calcular"))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            //System.out.println("Enviando solicitud al backend..."); // Debug
            logger.info("Enviando solicitud al backend...");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println("Respuesta recibida del backend: " + response.body()); // Debug
            logger.info("Respuesta recibida del backend: " + response.body());

            if (response.statusCode() == 200) {
                Map<String, Object> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
                //System.out.println("Datos procesados correctamente en el backend: " + responseMap); // Debug
                //System.out.println("Proporciones enviadas por el backend: " + responseMap.get("proporciones")); // Debug
                logger.info("Datos procesados correctamente en el backend: " + responseMap);
                logger.info("Proporciones enviadas por el backend: " + responseMap.get("proporciones"));

                return Optional.of(responseMap);
            } else {
                Notification.show("Error al calcular la simulación: " + response.statusCode());
                //System.out.println("Error al calcular la simulación: " + response.body()); // Debug
                logger.error("Error al calcular la simulación: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al calcular la simulación: " + e.getMessage());
            //System.out.println("Error al calcular la simulación: " + e.getMessage()); // Debug
            logger.error("Error al calcular la simulación: " + e.getMessage());
        }
        return Optional.empty();
    }




    // Metodo para exportar la simulación como PDF
    public void exportarSimulacionPdf(Map<String, Object> simulacionData) {
        //System.out.println("Exportando simulación a PDF con datos: " + simulacionData); // Debug
        logger.info("Exportando simulación a PDF con datos: " + simulacionData);
        try {
            if (!simulacionData.containsKey("ingresos") || !simulacionData.containsKey("mesesSimulacion") ||
                    !simulacionData.containsKey("gastosClasificados") || simulacionData.get("gastosClasificados") == null) {
                Notification.show("Error: Datos incompletos para la exportación.");
                //System.out.println("Datos incompletos para la exportación: " + simulacionData); // Debug
                logger.error("Datos incompletos para la exportación: " + simulacionData);
                return;
            }

            String json = objectMapper.writeValueAsString(simulacionData); // Serializar datos
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/simulacion/exportar"))
                    .header("Authorization", "Bearer " + getToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            //System.out.println("Enviando solicitud para generar PDF..."); // Debug
            logger.info("Enviando solicitud para generar PDF...");
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                byte[] pdfBytes = response.body();
                //System.out.println("PDF recibido correctamente del backend."); // Debug
                logger.info("PDF recibido correctamente del backend.");

                StreamResource pdfResource = new StreamResource("simulacion.pdf", () -> new ByteArrayInputStream(pdfBytes));
                pdfResource.setContentType("application/pdf");
                pdfResource.setCacheTime(0);

                StreamRegistration registration = UI.getCurrent().getSession().getResourceRegistry().registerResource(pdfResource);
                String pdfUrl = registration.getResourceUri().toString();
                UI.getCurrent().getPage().open(pdfUrl, "_blank");
            } else {
                Notification.show("Error al exportar el PDF: " + response.statusCode());
                //System.out.println("Error al exportar el PDF: " + response.body()); // Debug
                logger.error("Error al exportar el PDF: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Error al exportar el PDF: " + e.getMessage());
            //System.out.println("Error al exportar el PDF: " + e.getMessage()); // Debug
            logger.error("Error al exportar el PDF: " + e.getMessage());
        }
    }


    // Obtener el token de autenticación
    private String getToken() {
        return (String) UI.getCurrent().getSession().getAttribute("authToken");
    }
}
