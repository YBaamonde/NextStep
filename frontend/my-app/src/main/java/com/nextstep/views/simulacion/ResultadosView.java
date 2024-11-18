package com.nextstep.views.simulacion;

// Vista con los resultados de la simulación

import com.nextstep.services.AuthService;
import com.nextstep.services.SimulacionService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.*;

@Route("resultados")
@PageTitle("Resultados de Simulación | NextStep")
@CssImport("./themes/nextstepfrontend/resultados-view.css")
public class ResultadosView extends VerticalLayout implements BeforeEnterObserver {

    private Span balanceLabel;
    private Span metaAhorroLabel; // Mostrar meta de ahorro
    private Div recomendacionesContainer;
    private Map<String, Object> simulacionData; // Guardar datos de simulación para exportar

    public ResultadosView() {
        setClassName("resultados-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Navbar
        AuthService authService = new AuthService();
        MainNavbar navbar = new MainNavbar(authService);
        add(navbar);

        // Contenedor principal de resultados
        Div resultadosContainer = new Div();
        resultadosContainer.setClassName("resultados-container");

        balanceLabel = new Span();
        balanceLabel.addClassName("resultado-balance");

        metaAhorroLabel = new Span(); // Nuevo elemento para meta de ahorro
        metaAhorroLabel.addClassName("resultado-meta-ahorro");

        Span recomendacionesLabel = new Span("Recomendaciones:");
        recomendacionesLabel.addClassName("resultado-recomendaciones-title");

        recomendacionesContainer = new Div();
        recomendacionesContainer.setClassName("recomendaciones-container");

        // Botones para exportar y nueva simulación
        Button exportarPdfButton = new Button("Exportar PDF", event -> exportarPdf());
        exportarPdfButton.addClassName("export-pdf-button");

        Button nuevaSimulacionButton = new Button("Nueva Simulación", event -> UI.getCurrent().navigate("simulacion"));
        nuevaSimulacionButton.addClassName("nueva-simulacion-button");

        HorizontalLayout buttonLayout = new HorizontalLayout(exportarPdfButton, nuevaSimulacionButton);
        buttonLayout.addClassName("button-layout");

        resultadosContainer.add(balanceLabel, metaAhorroLabel, recomendacionesLabel, recomendacionesContainer, buttonLayout);
        add(resultadosContainer);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        simulacionData = (Map<String, Object>) UI.getCurrent().getSession().getAttribute("simulacionData");

        if (simulacionData == null || simulacionData.isEmpty()) {
            Notification.show("No se encontraron datos de simulación. Por favor, intente nuevamente.");
            UI.getCurrent().navigate("simulacion");
            return;
        }

        // Actualizar las etiquetas y recomendaciones
        balanceLabel.setText("Balance Proyectado: " + simulacionData.get("balanceProyectado") + " €");
        metaAhorroLabel.setText("Meta de Ahorro: " + simulacionData.get("metaAhorro") + " €");

        List<String> recomendaciones = (List<String>) simulacionData.get("recomendaciones");
        recomendacionesContainer.removeAll();
        recomendaciones.forEach(rec -> {
            Span recLabel = new Span("- " + rec.trim());
            recLabel.addClassName("resultado-recomendacion");
            recomendacionesContainer.add(recLabel);
        });
    }

    private void exportarPdf() {
        SimulacionService simulacionService = new SimulacionService();

        if (simulacionData == null || simulacionData.isEmpty()) {
            Notification.show("No se encontraron datos de simulación para exportar.");
            return;
        }

        simulacionService.exportarSimulacionPdf(simulacionData);
    }

}
