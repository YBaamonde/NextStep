package com.nextstep.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm; // Si tienes un formulario de registro, reemplaza esto
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
        H1 title = new H1("Register");
        layout.add(title);

        // Campos del formulario de registro
        TextField usernameField = new TextField("Username");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField passwordField2 = new PasswordField("Confirm Password");

        layout.add(usernameField, emailField, passwordField, passwordField2);

        // Botón para registrarse
        Button registerButton = new Button("Register", event -> {
            // Lógica para registrar al usuario
            // Aquí puedes agregar la lógica para el registro
        });

        // Estilo del botón de registro
        registerButton.addClassName("register-button2");

        // Añadir el botón al layout
        layout.add(registerButton);

        // Añadir el layout a la vista
        add(layout);
    }
}
