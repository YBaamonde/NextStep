package com.nextstep.views;

import com.nextstep.config.SecurityUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("admin")
public class AdminView extends VerticalLayout {

    public AdminView() {
        // Obtener el token almacenado en la sesión
        String token = (String) UI.getCurrent().getSession().getAttribute("authToken");

        // Verificar si el token existe y si el usuario tiene el rol "admin"
        if (token == null || !SecurityUtils.hasRole("admin", token)) {
            UI.getCurrent().navigate("login");
            //UI.getCurrent().getPage().reload(); // Recargar la página para asegurar la navegación
            return;
        }

        // Si el usuario tiene el rol "admin", mostrar el contenido de la vista
        Span adminLabel = new Span("Bienvenido a la vista de administrador");
        add(adminLabel); // Añadir el Span al layout

        // Aquí puedes añadir más componentes o lógica para el contenido de la vista de admin
    }
}
