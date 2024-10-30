package com.nextstep.config;

import com.nextstep.views.LoginView;
import com.nextstep.views.RegisterView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SecureRouteAccessControl implements BeforeEnterListener {

    // Definimos las vistas públicas a las que se puede acceder sin autenticación
    private static final Set<Class<?>> PUBLIC_VIEWS = Set.of(LoginView.class, RegisterView.class);

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        VaadinSession session = VaadinSession.getCurrent();
        String token = (String) session.getAttribute("authToken");

        // Si el usuario no tiene un token de autenticación y la vista no es pública, redirige al login
        if (token == null && !PUBLIC_VIEWS.contains(event.getNavigationTarget())) {
            Notification.show("Por favor, inicia sesión para acceder a esta página.");
            event.rerouteTo("login");  // Redirige a la vista de login
        }
    }
}

