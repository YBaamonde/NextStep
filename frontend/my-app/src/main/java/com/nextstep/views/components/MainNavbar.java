package com.nextstep.views.components;

import com.nextstep.services.AuthService;
import com.nextstep.services.InAppNotifService;
import com.nextstep.services.NotifConfigService;
import com.nextstep.views.GastosView;
import com.nextstep.views.InicioView;
import com.nextstep.views.PagosView;
import com.nextstep.views.PerfilView;
import com.nextstep.views.simulacion.SimulacionView;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.theme.lumo.LumoIcon;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CssImport("./themes/nextstepfrontend/navbar.css")
public class MainNavbar extends VerticalLayout {

    private final AuthService authService;
    private final InAppNotifService notifService; // Servicio para obtener notificaciones
    private final NotifConfigService notifConfigService; // Servicio para configuración
    private Avatar profileAvatar;
    private Button bellButton; // Botón para la campana
    private ContextMenu notificationMenu; // Menú desplegable para notificaciones

    public MainNavbar(AuthService authService, InAppNotifService notifService) {
        this.authService = authService;
        this.notifService = notifService;
        this.notifConfigService = new NotifConfigService(); // Inicializar servicio de configuración

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

        // Botón de notificaciones
        createNotificationButton();

        // Avatar para perfil (inicialmente sin iniciales)
        profileAvatar = new Avatar();
        Button avatarButton = new Button(profileAvatar);
        avatarButton.addClickListener(e -> avatarButton.getUI().ifPresent(ui -> ui.navigate("perfil")));
        avatarButton.getStyle().set("border", "none").set("background", "none").set("padding", "0");
        profileAvatar.addClassName("custom-avatar");

        HorizontalLayout navbar = new HorizontalLayout(logo, links);
        navbar.setAlignItems(Alignment.CENTER);
        navbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        navbar.setWidthFull();

        // Agregar campana y avatar solo si corresponde
        if (bellButton != null) {
            navbar.add(bellButton);
        }
        navbar.add(avatarButton);

        return navbar;
    }

    private void createNotificationButton() {
        // Verificar configuración de notificaciones In-App
        Integer usuarioId = authService.getUserId();
        Optional<Map<String, Object>> configOpt = notifConfigService.obtenerConfiguracion(usuarioId);

        if (configOpt.isPresent() && Boolean.TRUE.equals(configOpt.get().get("inAppActivas"))) {
            // Crear el botón de la campana
            bellButton = new Button(LumoIcon.BELL.create());
            bellButton.addClassName("notification-button");

            // Crear el menú desplegable
            notificationMenu = new ContextMenu();
            notificationMenu.setOpenOnClick(true); // Configurar apertura al hacer clic
            notificationMenu.setTarget(bellButton); // Asociar el menú al botón

            // Actualizar contenido del menú
            updateNotificationContent();
        } else {
            // Ocultar la campana si las notificaciones In-App están desactivadas
            bellButton = null;
        }
    }

    private void updateNotificationContent() {
        Integer usuarioId = authService.getUserId();

        // Obtener notificaciones desde el backend
        List<Map<String, Object>> notificaciones = notifService.obtenerNotificaciones(usuarioId);

        // Filtrar notificaciones expiradas en el frontend
        notificaciones = notifService.filtrarNotificacionesExpiradas(notificaciones);

        long noLeidas = notifService.contarNotificacionesNoLeidas(usuarioId);

        // Actualizar el contador de notificaciones no leídas
        if (noLeidas > 0) {
            bellButton.setText(String.valueOf(noLeidas)); // Mostrar número de notificaciones no leídas
        } else {
            bellButton.setText(""); // No mostrar texto si no hay notificaciones
        }

        // Limpiar el contenido del menú existente
        notificationMenu.removeAll();

        // Agregar notificaciones al menú
        if (notificaciones.isEmpty()) {
            notificationMenu.addItem("No hay notificaciones");
        } else {
            for (Map<String, Object> notificacion : notificaciones) {
                String titulo = (String) notificacion.get("titulo");
                String mensaje = (String) notificacion.get("mensaje");
                Integer id = (Integer) notificacion.get("id");

                // Agregar al menú
                notificationMenu.addItem(titulo + ": " + mensaje, e -> {
                    // Lógica para marcar como leída
                    notifService.marcarComoLeida(id);
                    updateNotificationContent(); // Actualizar contenido después de marcar como leída
                });
            }
        }
    }

    private HorizontalLayout createMobileNavbar() {
        // Crear botones con iconos
        Button homeButton = new Button(VaadinIcon.HOME.create());
        Button gastosButton = new Button(VaadinIcon.WALLET.create());
        Button pagosButton = new Button(VaadinIcon.CREDIT_CARD.create());
        Button simulacionButton = new Button(VaadinIcon.LINE_BAR_CHART.create());
        Button perfilButton = new Button(VaadinIcon.USER.create());

        // Aplicar estilos a los iconos para que sean negros
        Stream.of(homeButton, gastosButton, pagosButton, simulacionButton, perfilButton)
                .forEach(button -> {
                    button.getStyle().set("color", "black"); // Cambiar el color del icono a negro
                    button.getStyle().set("background-color", "transparent"); // Hacer que el fondo sea transparente
                    button.getStyle().set("border", "none"); // Eliminar bordes
                });

        // Establecer navegaciones
        homeButton.addClickListener(e -> homeButton.getUI().ifPresent(ui -> ui.navigate(InicioView.class)));
        gastosButton.addClickListener(e -> gastosButton.getUI().ifPresent(ui -> ui.navigate(GastosView.class)));
        pagosButton.addClickListener(e -> pagosButton.getUI().ifPresent(ui -> ui.navigate(PagosView.class)));
        simulacionButton.addClickListener(e -> simulacionButton.getUI().ifPresent(ui -> ui.navigate(SimulacionView.class)));
        perfilButton.addClickListener(e -> perfilButton.getUI().ifPresent(ui -> ui.navigate(PerfilView.class)));

        // Crear la barra de navegación
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
