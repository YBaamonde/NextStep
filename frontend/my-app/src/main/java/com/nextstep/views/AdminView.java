package com.nextstep.views;

import com.nextstep.config.SecurityUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("admin")
public class AdminView extends VerticalLayout {

    public AdminView() {
        // Obtener el token almacenado en la sesión
        String token = (String) UI.getCurrent().getSession().getAttribute("authToken");

        // Verificar si el token existe y si el usuario tiene el rol "admin"
        if (token == null || !SecurityUtils.hasRole("admin", token)) {
            // Mostrar una notificación informando al usuario
            Notification.show("Acceso denegado. Redirigiendo al login...");

            // Ejecutar la redirección de forma asíncrona
            UI.getCurrent().access(() -> {
                UI.getCurrent().navigate("login");
            });

            // Salir del constructor para que no se procese el contenido de la vista
            return;
        }

        // Si el usuario tiene el rol "admin", mostrar el contenido de la vista
        Span adminLabel = new Span("Bienvenido a la vista de administrador");
        add(adminLabel); // Añadir el Span al layout
    }
}
