package com.nextstep.views;

import com.nextstep.views.temp.InicioView;
import com.nextstep.views.temp.PagosView;
import com.nextstep.views.temp.SimulacionView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
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

        // Navbar principal para pantallas grandes
        FlexLayout navbar = createNavbar();
        navbar.addClassName("navbar");

        // Mobile navbar para pantallas pequeñas
        FlexLayout mobileNavbar = createMobileNavbar();
        mobileNavbar.addClassName("mobile-navbar");

        // Paneles para las categorías
        FlexLayout panelContainer = createPanelContainer();
        panelContainer.addClassName("panel-container");

        // Botón para agregar categoría
        Button addCategoryButton = new Button("Agregar Categoría");
        addCategoryButton.addClassName("categoria-button");

        // Agregar todos los elementos al layout
        add(navbar, panelContainer, addCategoryButton, mobileNavbar);
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

    private FlexLayout createMobileNavbar() {
        FlexLayout mobileNavbar = new FlexLayout();
        mobileNavbar.setWidthFull();
        mobileNavbar.setJustifyContentMode(FlexLayout.JustifyContentMode.AROUND);
        mobileNavbar.setAlignItems(Alignment.CENTER);

        // Iconos para la navegación en móviles
        Button homeButton = new Button(new Icon(VaadinIcon.HOME));
        Button gastosButton = new Button(new Icon(VaadinIcon.CREDIT_CARD));
        Button pagosButton = new Button(new Icon(VaadinIcon.MONEY));
        Button simulacionButton = new Button(new Icon(VaadinIcon.LINE_CHART));

        mobileNavbar.add(homeButton, gastosButton, pagosButton, simulacionButton);
        return mobileNavbar;
    }

    private FlexLayout createPanelContainer() {
        FlexLayout panelContainer = new FlexLayout();
        panelContainer.setFlexDirection(FlexLayout.FlexDirection.ROW);
        panelContainer.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        panelContainer.setJustifyContentMode(FlexLayout.JustifyContentMode.AROUND);
        panelContainer.setAlignItems(FlexLayout.Alignment.CENTER);

        // Ejemplo de paneles
        for (int i = 0; i < 3; i++) {
            Div panel = new Div();
            panel.addClassName("panel");
            panel.setText("Categoría " + (i + 1));
            panelContainer.add(panel);
        }

        return panelContainer;
    }

}
