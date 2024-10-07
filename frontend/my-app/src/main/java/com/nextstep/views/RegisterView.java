package com.nextstep.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.nextstep.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

@CssImport("./themes/nextstepfrontend/login-view.css")
@Route("register")
public class RegisterView extends Div {

    private final AuthService authService;  // Inyecta el servicio AuthService del frontend

    @Autowired
    public RegisterView(AuthService authService) {
        this.authService = authService;

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

        // Campos del formulario
        TextField usernameField = new TextField("Username");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm password");

        layout.add(usernameField, emailField, passwordField, confirmPasswordField);

        // Botón para registrarse
        Button registerButton = new Button("Registrar", event -> {
            String username = usernameField.getValue();
            String email = emailField.getValue();
            String password = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            // Llama al servicio de registro del frontend
            authService.register(username, email, password, confirmPassword);
        });

        registerButton.addClassName("register-summit-button");
        layout.add(registerButton);

        // Enlace para volver al login
        Button loginButton = new Button("Ya tengo cuenta", event -> getUI().ifPresent(ui -> ui.navigate("login")));
        loginButton.addClassName("login-link");
        layout.add(loginButton);

        add(layout);
    }
}
