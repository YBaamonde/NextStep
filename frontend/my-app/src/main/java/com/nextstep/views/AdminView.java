package com.nextstep.views;

import com.nextstep.config.SecurityUtils;
import com.nextstep.services.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Route("admin")
public class AdminView extends VerticalLayout {

    private final AuthService authService;

    public AdminView(AuthService authService) {
        this.authService = authService;

        // Verificar si el usuario tiene el rol de admin
        if (!SecurityUtils.hasRole("admin")) {
            UI.getCurrent().navigate("login");
            return; // Salir del constructor si no es admin
        }

        // Crear el título
        H1 title = new H1("Admin Panel");

        // Crear el botón de obtener usuarios
        Button getUsersButton = new Button("Obtener Usuarios", event -> fetchUsers());

        add(title, getUsersButton);
    }

    private void fetchUsers() {
        try {
            // Crear una solicitud autenticada al backend para obtener la lista de usuarios
            HttpRequest request = authService.createAuthenticatedRequest("/admin/users");

            // Enviar la solicitud y manejar la respuesta
            HttpResponse<String> response = authService.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Muestra la lista de usuarios
                Notification.show("Usuarios obtenidos: " + response.body());
            } else {
                Notification.show("Error al obtener usuarios. Estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Notification.show("Ocurrió un error al obtener usuarios: " + e.getMessage());
        }
    }
}
