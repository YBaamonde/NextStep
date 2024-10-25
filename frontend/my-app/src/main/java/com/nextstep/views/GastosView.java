package com.nextstep.views;

import com.nextstep.views.temp.InicioView;
import com.nextstep.views.temp.PagosView;
import com.nextstep.views.temp.SimulacionView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("gastos")
@PageTitle("Gastos | NextStep")
@CssImport("./themes/nextstepfrontend/gastos-view.css")
public class GastosView extends VerticalLayout {

    public GastosView() {
        setClassName("gastos-container");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Navbar
        FlexLayout navbar = createNavbar();
        navbar.addClassName("navbar");

        // Panel izquierdo (Agregar Gasto)
        Div leftPanel = new Div();
        leftPanel.addClassName("panel");
        leftPanel.addClassName("acrilico");
        Button addGastoButton = new Button("Añadir Gasto");
        addGastoButton.addClassName("action-button");
        leftPanel.add(addGastoButton);

        // Panel central (Gastos registrados)
        Div centerPanel = new Div();
        centerPanel.addClassName("panel");
        centerPanel.addClassName("acrilico");
        H2 gastosLabel = new H2("Gastos registrados");
        gastosLabel.addClassName("section-title");

        // Ejemplo de gasto (en la práctica, este se generaría dinámicamente)
        Div gastoItem = new Div();
        gastoItem.addClassName("gasto-item");
        Span gastoName = new Span("Transporte: 50€");
        Button editButton = new Button("Editar");
        Button deleteButton = new Button("Eliminar");
        editButton.addClassName("action-button");
        deleteButton.addClassName("action-button");

        Div actions = new Div(editButton, deleteButton);
        gastoItem.add(gastoName, actions);
        centerPanel.add(gastosLabel, gastoItem);

        // Panel derecho (Categorías)
        Div rightPanel = new Div();
        rightPanel.addClassName("panel");
        rightPanel.addClassName("acrilico");
        H2 categoriesLabel = new H2("Categorías");
        categoriesLabel.addClassName("section-title");

        // Ejemplo de categoría (en la práctica, este se generaría dinámicamente)
        Button categoryButton = new Button("Alimentación");
        categoryButton.addClassName("action-button");
        Button addCategoryButton = new Button("Agregar Categoría");
        addCategoryButton.addClassName("action-button");

        rightPanel.add(categoriesLabel, categoryButton, addCategoryButton);

        // Agregar los paneles al layout principal
        add(navbar, leftPanel, centerPanel, rightPanel);
    }

    private FlexLayout createNavbar() {
        FlexLayout navbar = new FlexLayout();
        navbar.setWidthFull();
        navbar.setJustifyContentMode(FlexLayout.JustifyContentMode.BETWEEN);
        navbar.setAlignItems(Alignment.CENTER);

        // Enlaces de navegación
        RouterLink homeLink = new RouterLink("Inicio", InicioView.class);
        RouterLink gastosLink = new RouterLink("Gastos", GastosView.class);
        RouterLink pagosLink = new RouterLink("Pagos", PagosView.class);
        RouterLink simulacionLink = new RouterLink("Simulación", SimulacionView.class);

        // Logo de la aplicación
        H1 logo = new H1("NextStep");
        logo.getStyle().set("margin", "0");

        // Añadir elementos a la navbar
        navbar.add(logo, homeLink, gastosLink, pagosLink, simulacionLink);
        return navbar;
    }
}
