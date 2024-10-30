package com.nextstep.config;

import com.nextstep.views.LoginView;
import com.nextstep.views.RegisterView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SecureRouteAccessControl implements BeforeEnterListener {

    // Vistas públicas a las que se permite el acceso sin autenticación
    private static final Set<Class<?>> PUBLIC_VIEWS = Set.of(LoginView.class, RegisterView.class);

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String token = (String) VaadinSession.getCurrent().getAttribute("authToken");

        if (token == null && !PUBLIC_VIEWS.contains(event.getNavigationTarget())) {
            UI.getCurrent().access(() -> Notification.show("Por favor, inicia sesión para continuar."));
            event.forwardTo("login");
        }
    }
}