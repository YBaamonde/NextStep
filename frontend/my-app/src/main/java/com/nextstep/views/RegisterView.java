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

@CssImport("./themes/nextstepfrontend/login-view.css")
@Route("register")
public class RegisterView extends Div {

    public RegisterView() {
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

        // Campos del formulario de registro
        TextField usernameField = new TextField("Username");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField passwordField2 = new PasswordField("Confirm password");

        layout.add(usernameField, emailField, passwordField, passwordField2);

        // Botón para registrarse
        Button registerButton = new Button("Registrarse", event -> {
            // Lógica para registrar al usuario
            // Aquí puedes agregar la lógica para el registro
        });

        // Estilo del botón de registro
        registerButton.addClassName("register-summit-button");

        // Añadir el botón al layout
        layout.add(registerButton);

        // Enlace para volver al login
        Button loginButton = new Button("Ya tengo cuenta", event -> {
            // Redirige al usuario a la vista de registro
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        // Estilo del botón de login igual que el de registro
        loginButton.addClassName("login-link");

        layout.add(loginButton);

        // Añadir el layout a la vista
        add(layout);
    }
}
