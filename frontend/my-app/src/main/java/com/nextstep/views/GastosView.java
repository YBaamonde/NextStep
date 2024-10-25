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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
        Div leftPanel = createPanel("Añadir Gasto", "Añadir Gasto");

        // Panel central (Gastos registrados)
        Div centerPanel = createGastoPanel();

        // Panel derecho (Categorías)
        Div rightPanel = createPanel("Categorías", "Agregar Categoría");

        // Agregar los paneles al layout principal
        add(navbar, leftPanel, centerPanel, rightPanel);

        // Cajón de navegación para móviles
        FlexLayout mobileNavbar = createMobileNavbar();
        mobileNavbar.addClassName("mobile-navbar");
        add(mobileNavbar);
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
        logo.getStyle().set("margin", "0").set("font-weight", "bold").set("color", "#FF5722");

        // Añadir elementos a la navbar
        navbar.add(logo, homeLink, gastosLink, pagosLink, simulacionLink);
        return navbar;
    }

    private FlexLayout createMobileNavbar() {
        FlexLayout mobileNavbar = new FlexLayout();
        mobileNavbar.setJustifyContentMode(FlexLayout.JustifyContentMode.AROUND);

        Button homeButton = new Button(new Icon(VaadinIcon.HOME));
        Button gastosButton = new Button(new Icon(VaadinIcon.WALLET));
        Button pagosButton = new Button(new Icon(VaadinIcon.CREDIT_CARD));
        Button simulacionButton = new Button(new Icon(VaadinIcon.LINE_CHART));

        homeButton.addClickListener(e -> homeButton.getUI().ifPresent(ui -> ui.navigate(InicioView.class)));
        gastosButton.addClickListener(e -> gastosButton.getUI().ifPresent(ui -> ui.navigate(GastosView.class)));
        pagosButton.addClickListener(e -> pagosButton.getUI().ifPresent(ui -> ui.navigate(PagosView.class)));
        simulacionButton.addClickListener(e -> simulacionButton.getUI().ifPresent(ui -> ui.navigate(SimulacionView.class)));

        mobileNavbar.add(homeButton, gastosButton, pagosButton, simulacionButton);
        return mobileNavbar;
    }

    private Div createPanel(String titleText, String buttonText) {
        Div panel = new Div();
        panel.addClassName("panel");
        panel.addClassName("acrilico");

        H2 title = new H2(titleText);
        title.addClassName("section-title");
        Button actionButton = new Button(buttonText);
        actionButton.addClassName("action-button");

        panel.add(title, actionButton);
        return panel;
    }

    private Div createGastoPanel() {
        Div panel = new Div();
        panel.addClassName("panel");
        panel.addClassName("acrilico");

        H2 title = new H2("Gastos registrados");
        title.addClassName("section-title");

        Div gastoItem = new Div();
        gastoItem.addClassName("gasto-item");
        Span gastoName = new Span("Transporte: 50€");
        Button editButton = new Button("Editar");
        Button deleteButton = new Button("Eliminar");
        editButton.addClassName("action-button");
        deleteButton.addClassName("action-button");

        Div actions = new Div(editButton, deleteButton);
        gastoItem.add(gastoName, actions);

        panel.add(title, gastoItem);
        return panel;
    }
}
