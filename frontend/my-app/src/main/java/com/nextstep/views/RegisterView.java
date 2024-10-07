package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@CssImport("./themes/nextstepfrontend/login-view.css")
@Route("register")
public class RegisterView extends Div {

    private final AuthService authService;  // Referencia al servicio de autenticación

    private final TextField nombreField;     // Campo para el nombre
    private final TextField usernameField;   // Campo para el nombre de usuario
    private final TextField emailField;      // Campo para el email
    private final PasswordField passwordField;       // Campo para la contraseña
    private final PasswordField confirmPasswordField; // Campo para confirmar contraseña

    // Correo del administrador
    private final String adminEmail = "admin@example.com"; // Cambia este correo al correo que quieres que tenga permisos de admin

    @Autowired
    public RegisterView(AuthService authService) {
        this.authService = authService;  // Inicializa AuthService

        // Inicializar los campos
        nombreField = new TextField("Nombre");
        usernameField = new TextField("Username");
        emailField = new TextField("Email");
        passwordField = new PasswordField("Password");
        confirmPasswordField = new PasswordField("Confirm password");

        // Añadir clase CSS a la vista
        addClassName("login-view");

        // Crear un layout para centrar el formulario
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.addClassName("login-container");

        // Encabezado
        H1 title = new H1("Registro");
        title.addClassName("titulo-registro");
        layout.add(title);

        // Añadir los campos al layout
        layout.add(nombreField, usernameField, emailField, passwordField, confirmPasswordField);

        // Botón para registrarse
        Button registerButton = new Button("Registrar", event -> {
            String nombre = nombreField.getValue();
            String username = usernameField.getValue();
            String email = emailField.getValue();
            String password = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            // Validación simple de contraseñas
            if (!password.equals(confirmPassword)) {
                Notification.show("Las contraseñas no coinciden");
            } else {
                // Asignar rol basado en el correo
                String rol = email.equals(adminEmail) ? "admin" : "normal";

                // Invocar el servicio de registro
                authService.register(username, email, password, confirmPassword, rol);
            }
        });

        // Estilo del botón de registro
        registerButton.addClassName("register-summit-button");

        // Añadir el botón al layout
        layout.add(registerButton);

        // Enlace para volver al login
        Button loginButton = new Button("Ya tengo cuenta", event -> {
            // Redirige al usuario a la vista de login
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        // Estilo del botón de login igual que el de registro
        loginButton.addClassName("login-link");

        layout.add(loginButton);

        // Añadir el layout a la vista
        add(layout);
    }
}
