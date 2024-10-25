package com.nextstep.views.components;

import com.nextstep.views.GastosView;
import com.nextstep.views.temp.InicioView;
import com.nextstep.views.temp.PagosView;
import com.nextstep.views.temp.SimulacionView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainNavbar extends AppLayout {

    public MainNavbar() {
        // Logo y título
        H1 logo = new H1("NextStep");
        logo.getStyle().set("font-size", "var(--lumo-font-size-xl)")
                .set("font-weight", "bold").set("margin", "0 var(--lumo-space-m)");

        // Enlaces de navegación
        RouterLink homeLink = new RouterLink("Inicio", InicioView.class);
        RouterLink gastosLink = new RouterLink("Gastos", GastosView.class);
        RouterLink pagosLink = new RouterLink("Pagos", PagosView.class);
        RouterLink simulacionLink = new RouterLink("Simulación", SimulacionView.class);

        HorizontalLayout navLinks = new HorizontalLayout(homeLink, gastosLink, pagosLink, simulacionLink);
        navLinks.setSpacing(true);

        HorizontalLayout navbar = new HorizontalLayout(logo, navLinks);
        navbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        navbar.setWidthFull();
        navbar.getStyle().set("background-color", "white")
                .set("padding", "var(--lumo-space-s)")
                .set("box-shadow", "0 2px 10px rgba(0, 0, 0, 0.1)")
                .set("border-radius", "12px")
                .set("margin", "var(--lumo-space-m) auto");

        addToNavbar(navbar);
    }
}
