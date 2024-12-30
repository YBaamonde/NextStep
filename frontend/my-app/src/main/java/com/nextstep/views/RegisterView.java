package com.nextstep.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.nextstep.services.AuthService;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

@CssImport("./themes/nextstepfrontend/login-view.css")
@Route("register")
@PageTitle("Registro | NextStep")
public class RegisterView extends Div {

    private final AuthService authService;

    @Autowired
    public RegisterView(AuthService authService) {
        this.authService = authService;

        addClassName("login-view");

        // Verificar si ya existe una sesión
        String token = (String) VaadinSession.getCurrent().getAttribute("authToken");

        if (token != null) {
            // Si existe un token, significa que el usuario está autenticado
            VaadinSession.getCurrent().getSession().invalidate(); // Invalidar la sesión actual
            VaadinSession.getCurrent().close();  // Cerrar la sesión
             UI.getCurrent().getPage().reload(); // Recargar la página para iniciar una sesión limpia
        }


        // Crear un layout para centrar el formulario
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.addClassName("login-container");

        // Encabezado
        H1 title = new H1("Registro");
        title.addClassName("titulo-registro");
        layout.add(title);

        // Campos del formulario
        TextField usernameField = new TextField("Username");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm password");

        layout.add(usernameField, emailField, passwordField, confirmPasswordField);

        // Botón para registrarse
        Button registerButton = new Button("Registrarse");
        registerButton.addClickListener(event -> {
            String username = usernameField.getValue();
            String email = emailField.getValue();
            String password = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            if (!password.equals(confirmPassword)) {
                Notification.show("Las contraseñas no coinciden", 3000, Notification.Position.MIDDLE);
            } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                Notification.show("Formato de correo electrónico inválido", 3000, Notification.Position.MIDDLE);
            } else if (password.length() < 8) {
                Notification.show("La contraseña debe tener al menos 8 caracteres", 3000, Notification.Position.MIDDLE);
            } else {
                // Usar AuthService para registrar al usuario
                authService.register(username, email, password);
            }
        });

        registerButton.addClassName("register-summit-button");
        layout.add(registerButton);

        // Enlace para volver al login
        Button loginButton = new Button("Ya tengo una cuenta", event -> getUI().ifPresent(ui -> ui.navigate("login")));
        loginButton.addClassName("login-link");
        layout.add(loginButton);

        add(layout);
    }
}
