package com.nextstep.views;

import com.nextstep.views.temp.InicioView;
import com.nextstep.views.temp.PagosView;
import com.nextstep.views.temp.SimulacionView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Gastos - NextStep")
@Route("gastos")
@CssImport("./themes/nextstepfrontend/gastos-view.css")
public class GastosView extends VerticalLayout {

    public GastosView() {
        // Configurar la barra de navegación
        HorizontalLayout navbar = createNavbar();
        navbar.addClassName("navbar");

        // Crear el contenido principal
        VerticalLayout mainContent = createMainContent();

        // Configurar la vista
        setSizeFull();
        add(navbar, mainContent);
        setClassName("gastos-view");
    }

    private HorizontalLayout createNavbar() {
        HorizontalLayout navbar = new HorizontalLayout();
        navbar.setWidth("80%");
        navbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        navbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        navbar.addClassName("navbar");

        // Logo
        Span logo = new Span("NextStep");
        logo.addClassName("navbar-logo");

        // Enlaces de navegación
        RouterLink inicioLink = new RouterLink("Inicio", InicioView.class);
        RouterLink gastosLink = new RouterLink("Gastos", GastosView.class);
        RouterLink pagosLink = new RouterLink("Pagos", PagosView.class);
        RouterLink simulacionLink = new RouterLink("Simulación", SimulacionView.class);

        gastosLink.addClassName("active");

        // Sección de enlaces
        HorizontalLayout links = new HorizontalLayout(inicioLink, gastosLink, pagosLink, simulacionLink);
        links.addClassName("navbar-links");

        // Icono de usuario
        Icon bellIcon = new Icon(VaadinIcon.BELL);
        bellIcon.addClassName("navbar-icon");

        Span userIcon = new Span("CL");
        userIcon.addClassName("navbar-user-icon");

        HorizontalLayout userSection = new HorizontalLayout(bellIcon, userIcon);
        userSection.setSpacing(true);
        userSection.addClassName("navbar-user-section");

        // Agregar a la barra de navegación
        navbar.add(logo, links, userSection);

        return navbar;
    }

    private VerticalLayout createMainContent() {
        VerticalLayout mainContentLayout = new VerticalLayout();
        mainContentLayout.addClassName("main-content");

        // Panel izquierdo para "Añadir Gasto"
        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.addClassName("left-panel");
        Div addGastoButton = new Div(new Text("Añadir Gasto"));
        addGastoButton.addClassName("add-button");
        leftPanel.add(addGastoButton);

        // Panel central para los gastos
        VerticalLayout centerPanel = new VerticalLayout();
        centerPanel.addClassName("center-panel");
        Div gastosLabel = new Div(new Text("Gastos registrados"));
        gastosLabel.addClassName("section-title");
        centerPanel.add(gastosLabel);

        // Ejemplo de un gasto
        Div gastoItem = new Div();
        Span gastoName = new Span("Transporte: 50€");
        HorizontalLayout actions = new HorizontalLayout(new Div(new Text("Editar")), new Div(new Text("Eliminar")));
        actions.addClassName("gasto-actions");
        gastoItem.add(gastoName, actions);
        centerPanel.add(gastoItem);

        // Panel derecho para las categorías
        VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.addClassName("right-panel");
        Div categoriesLabel = new Div(new Text("Categorías"));
        categoriesLabel.addClassName("section-title");
        rightPanel.add(categoriesLabel);

        // Ejemplo de una categoría
        Span categoryChip = new Span("Alimentación");
        categoryChip.addClassName("category-chip");
        rightPanel.add(categoryChip);

        Div addCategoryButton = new Div(new Text("Agregar Categoría"));
        addCategoryButton.addClassName("add-button");
        rightPanel.add(addCategoryButton);

        // Añadir los paneles al layout principal
        HorizontalLayout panelsLayout = new HorizontalLayout(leftPanel, centerPanel, rightPanel);
        panelsLayout.addClassName("panels-layout");

        mainContentLayout.add(panelsLayout);
        return mainContentLayout;
    }
}
