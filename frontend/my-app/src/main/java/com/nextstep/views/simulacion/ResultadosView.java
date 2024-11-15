package com.nextstep.views.simulacion;

// Vista con los resultados de la simulación

import com.nextstep.services.AuthService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.*;

@Route("resultados")
@PageTitle("Resultados | NextStep")
@CssImport("./themes/nextstepfrontend/resultados-view.css")
public class ResultadosView extends VerticalLayout {

    public ResultadosView() {
        setClassName("resultados-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Navbar
        AuthService authService = new AuthService();
        MainNavbar navbar = new MainNavbar(authService);
        add(navbar);

        // Obtener parámetros
        String balanceProyectado = UI.getCurrent().getInternals().getActiveViewLocation().getQueryParameters()
                .getParameters().getOrDefault("balanceProyectado", List.of("0")).get(0);
        String recomendaciones = UI.getCurrent().getInternals().getActiveViewLocation().getQueryParameters()
                .getParameters().getOrDefault("recomendaciones", List.of("")).get(0);

        // Contenedor principal
        Div resultadosContainer = new Div();
        resultadosContainer.setClassName("resultados-container");
        add(resultadosContainer);

        // Mostrar balance proyectado
        Div balanceDiv = new Div();
        balanceDiv.setClassName("resultado-balance");
        balanceDiv.setText("Balance Proyectado: " + balanceProyectado + " €");
        resultadosContainer.add(balanceDiv);

        // Mostrar recomendaciones
        Div recomendacionesTitleDiv = new Div();
        recomendacionesTitleDiv.setClassName("resultado-recomendaciones-title");
        recomendacionesTitleDiv.setText("Recomendaciones:");
        resultadosContainer.add(recomendacionesTitleDiv);

        Div recomendacionesDiv = new Div();
        recomendacionesDiv.setClassName("recomendaciones-container");
        Arrays.stream(recomendaciones.split(","))
                .map(String::trim)
                .forEach(rec -> {
                    Div recDiv = new Div();
                    recDiv.setClassName("resultado-recomendacion");
                    recDiv.setText("- " + rec);
                    recomendacionesDiv.add(recDiv);
                });

        resultadosContainer.add(recomendacionesDiv);
    }
}
