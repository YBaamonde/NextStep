package com.nextstep.views.simulacion;

// Vista con los resultados de la simulación

import com.nextstep.services.AuthService;
import com.nextstep.services.InAppNotifService;
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
import com.vaadin.flow.component.progressbar.ProgressBar;
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
    private Span metaAhorroLabel;
    private Div recomendacionesContainer;
    private Div progressBarsContainer;
    private ProgressBar esencialesBar;
    private ProgressBar opcionalesBar;
    private Map<String, Object> simulacionData;

    public ResultadosView() {
        setClassName("resultados-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        AuthService authService = new AuthService();
        InAppNotifService inAppNotifService = new InAppNotifService();

        // Agregar navbar en la parte superior
        MainNavbar navbar = new MainNavbar(authService, inAppNotifService);
        add(navbar);

        Div resultadosContainer = new Div();
        resultadosContainer.setClassName("resultados-container");

        balanceLabel = new Span();
        balanceLabel.addClassName("resultado-balance");

        metaAhorroLabel = new Span();
        metaAhorroLabel.addClassName("resultado-meta-ahorro");

        Span recomendacionesLabel = new Span("Recomendaciones:");
        recomendacionesLabel.addClassName("resultado-recomendaciones-title");

        recomendacionesContainer = new Div();
        recomendacionesContainer.setClassName("recomendaciones-container");

        progressBarsContainer = new Div();
        progressBarsContainer.setClassName("progress-bars-container");

        esencialesBar = new ProgressBar();
        esencialesBar.addClassName("esenciales-bar");

        opcionalesBar = new ProgressBar();
        opcionalesBar.addClassName("opcionales-bar");

        Button exportarPdfButton = new Button("Exportar PDF", event -> exportarPdf());
        exportarPdfButton.addClassName("export-pdf-button");

        Button nuevaSimulacionButton = new Button("Nueva Simulación", event -> UI.getCurrent().navigate("simulacion"));
        nuevaSimulacionButton.addClassName("nueva-simulacion-button");

        HorizontalLayout buttonLayout = new HorizontalLayout(exportarPdfButton, nuevaSimulacionButton);
        buttonLayout.addClassName("button-layout");

        resultadosContainer.add(balanceLabel, metaAhorroLabel, recomendacionesLabel, progressBarsContainer, recomendacionesContainer, buttonLayout);
        add(resultadosContainer);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Debug: Verificar si los datos de la simulación están disponibles en la sesión
        simulacionData = (Map<String, Object>) UI.getCurrent().getSession().getAttribute("simulacionData");
        System.out.println("Datos cargados en ResultadosView: " + simulacionData); // Debug

        if (simulacionData == null || simulacionData.isEmpty()) {
            Notification.show("No se encontraron datos de simulación.");
            UI.getCurrent().navigate("simulacion");
            return;
        }

        // Extraer y procesar datos
        double balanceProyectado = (double) simulacionData.getOrDefault("balanceProyectado", 0.0);
        double metaAhorro = (double) simulacionData.getOrDefault("metaAhorro", 0.0);

        // Verificar y extraer proporciones
        Map<String, Double> proporciones = (Map<String, Double>) simulacionData.getOrDefault("proporciones", new HashMap<>());
        double esencialesPorcentaje = proporciones.getOrDefault("esenciales", 0.0);
        double opcionalesPorcentaje = proporciones.getOrDefault("opcionales", 0.0);

        // Debug: Confirmar proporciones extraídas
        System.out.println("Proporciones extraídas: Esenciales = " + esencialesPorcentaje + ", Opcionales = " + opcionalesPorcentaje); // Debug

        // Actualizar componentes de la vista
        balanceLabel.setText("Balance Proyectado: " + balanceProyectado + " €");
        metaAhorroLabel.setText("Meta de Ahorro: " + metaAhorro + " €");

        // Actualizar barras de progreso
        esencialesBar.setValue(ajustarValorBarra(esencialesPorcentaje));
        opcionalesBar.setValue(ajustarValorBarra(opcionalesPorcentaje));

        Span esencialesLabel = new Span("Esenciales: " + String.format("%.1f", esencialesPorcentaje) + "%");
        esencialesLabel.addClassName("progreso-esenciales");

        Span opcionalesLabel = new Span("Opcionales: " + String.format("%.1f", opcionalesPorcentaje) + "%");
        opcionalesLabel.addClassName("progreso-opcionales");

        VerticalLayout esencialesContainer = new VerticalLayout(esencialesLabel, esencialesBar);
        VerticalLayout opcionalesContainer = new VerticalLayout(opcionalesLabel, opcionalesBar);

        progressBarsContainer.removeAll();
        progressBarsContainer.add(esencialesContainer, opcionalesContainer);

        // Procesar recomendaciones
        List<String> recomendaciones = (List<String>) simulacionData.get("recomendaciones");
        recomendacionesContainer.removeAll();
        recomendaciones.forEach(rec -> {
            Span recLabel = new Span("- " + rec.trim());
            recLabel.addClassName("resultado-recomendacion");
            recomendacionesContainer.add(recLabel);
        });

        // Debug: Confirmar que los datos se configuraron correctamente
        System.out.println("Vista ResultadosView configurada con datos: " + simulacionData); // Debug
    }

    private double ajustarValorBarra(double porcentaje) {
        if (porcentaje < 0.0) {
            return 0.0;
        } else if (porcentaje > 100.0) {
            return 1.0;
        }
        return porcentaje / 100.0;
    }



    private void exportarPdf() {
        System.out.println("Exportando PDF con datos: " + simulacionData);

        if (simulacionData == null || simulacionData.isEmpty()) {
            Notification.show("Error: Datos incompletos para la exportación.");
            return;
        }

        SimulacionService simulacionService = new SimulacionService();
        simulacionService.exportarSimulacionPdf(simulacionData);
    }
}
