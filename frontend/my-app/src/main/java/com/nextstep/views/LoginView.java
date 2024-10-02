package com.nextstep.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./themes/nextstepfrontend/login-view.css")
@Route("login")
public class LoginView extends Div {

    public LoginView() {
        addClassName("login-view");

        // Crear un layout para centrar el formulario
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.addClassName("login-container");


        // Botón para registrarse
        Button registerButton = new Button("Register", event -> {
            // Redirige al usuario a la vista de registro
            getUI().ifPresent(ui -> ui.navigate("register"));
        });

        // Estilo del botón de registro
        registerButton.addClassName("register-link");


        LoginForm loginForm = new LoginForm();
        loginForm.getElement().getThemeList().add("light");

        layout.add(loginForm, registerButton);
        add(layout);

    }
}
