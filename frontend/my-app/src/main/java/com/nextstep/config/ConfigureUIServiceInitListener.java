package com.nextstep.config;

// Esta clase asegura que el servicio de configuración se inicie al iniciar la aplicación

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        // Añade SecureRouteAccessControl como BeforeEnterListener para todas las rutas de Vaadin
        event.getSource().addUIInitListener(uiEvent ->
                uiEvent.getUI().addBeforeEnterListener(new SecureRouteAccessControl()));
    }
}

