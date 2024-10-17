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
import org.springframework.security.access.annotation.Secured;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Route("admin")
@Secured("admin")
public class AdminView extends VerticalLayout {

    public AdminView() {

        // Verificar si el usuario tiene el rol de admin
        if (!SecurityUtils.hasRole("admin")) {
            UI.getCurrent().navigate("login");
            return; // Salir del constructor si no es admin
        }


        // Continuar con el código de la vista admin...
        add(new H1("Bienvenido, Admin"));
    }
}

