package com.nextstep.views.components;

import com.nextstep.services.AuthService;
import com.nextstep.views.GastosView;
import com.nextstep.views.temp.InicioView;
import com.nextstep.views.temp.PagosView;
import com.nextstep.views.PerfilView;
import com.nextstep.views.temp.SimulacionView;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

import java.util.stream.Collectors;

@CssImport("./themes/nextstepfrontend/navbar.css")
public class MainNavbar extends VerticalLayout {

    private final AuthService authService; // Inyección de dependencia para AuthService
    private Avatar profileAvatar;

    public MainNavbar(AuthService authService) {
        this.authService = authService;
        setPadding(false);
        setSpacing(false);
        setWidthFull();
        setAlignItems(Alignment.CENTER);

        // Crear la navbar para pantallas grandes
        HorizontalLayout navbar = createNavbar();
        navbar.setClassName("navbar");

        // Crear el menú inferior para dispositivos móviles
        HorizontalLayout mobileNavbar = createMobileNavbar();
        mobileNavbar.setClassName("mobile-navbar");

        // Añadir ambos elementos
        add(navbar, mobileNavbar);

        // Configurar avatar con las iniciales del usuario
        setAvatarWithUsername();
    }

    private HorizontalLayout createNavbar() {
        H1 logo = new H1("NextStep");
        logo.getStyle().set("font-weight", "bold");

        // Enlaces de navegación
        RouterLink homeLink = new RouterLink("Inicio", InicioView.class);
        RouterLink gastosLink = new RouterLink("Gastos", GastosView.class);
        RouterLink pagosLink = new RouterLink("Pagos", PagosView.class);
        RouterLink simulacionLink = new RouterLink("Simulación", SimulacionView.class);

        HorizontalLayout links = new HorizontalLayout(homeLink, gastosLink, pagosLink, simulacionLink);
        links.addClassName("nav-links");
        links.setJustifyContentMode(JustifyContentMode.CENTER);
        links.setSpacing(true);

        // Avatar para perfil (inicialmente sin iniciales)
        profileAvatar = new Avatar();
        Button avatarButton = new Button(profileAvatar);
        avatarButton.addClickListener(e -> avatarButton.getUI().ifPresent(ui -> ui.navigate("perfil")));
        avatarButton.getStyle().set("border", "none").set("background", "none").set("padding", "0");
        profileAvatar.addClassName("custom-avatar");


        HorizontalLayout navbar = new HorizontalLayout(logo, links, avatarButton);
        navbar.setAlignItems(Alignment.CENTER);
        navbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        navbar.setWidthFull();
        return navbar;
    }

    private HorizontalLayout createMobileNavbar() {
        Button homeButton = new Button(VaadinIcon.HOME.create());
        Button gastosButton = new Button(VaadinIcon.WALLET.create());
        Button pagosButton = new Button(VaadinIcon.CREDIT_CARD.create());
        Button simulacionButton = new Button(VaadinIcon.LINE_BAR_CHART.create());
        Button perfilButton = new Button(VaadinIcon.USER.create());

        // Establecer navegaciones
        homeButton.addClickListener(e -> homeButton.getUI().ifPresent(ui -> ui.navigate(InicioView.class)));
        gastosButton.addClickListener(e -> gastosButton.getUI().ifPresent(ui -> ui.navigate(GastosView.class)));
        pagosButton.addClickListener(e -> pagosButton.getUI().ifPresent(ui -> ui.navigate(PagosView.class)));
        simulacionButton.addClickListener(e -> simulacionButton.getUI().ifPresent(ui -> ui.navigate(SimulacionView.class)));
        perfilButton.addClickListener(e -> perfilButton.getUI().ifPresent(ui -> ui.navigate(PerfilView.class)));

        HorizontalLayout mobileNavbar = new HorizontalLayout(homeButton, gastosButton, pagosButton, simulacionButton, perfilButton);
        mobileNavbar.setWidthFull();
        mobileNavbar.setJustifyContentMode(JustifyContentMode.AROUND);
        mobileNavbar.setAlignItems(Alignment.CENTER);
        return mobileNavbar;
    }

    private void setAvatarWithUsername() {
        String username = authService.getUsername();
        String initials = username.chars()
                .filter(Character::isUpperCase)
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());

        // Configurar ambas propiedades
        profileAvatar.setAbbreviation(initials);
        profileAvatar.setName(username.toUpperCase());
    }

}
