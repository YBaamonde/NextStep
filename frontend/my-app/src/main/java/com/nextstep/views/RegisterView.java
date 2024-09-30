package com.nextstep.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("register")
public class RegisterView extends Div {

    public RegisterView() {
        FormLayout formLayout = new FormLayout();

        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");

        Button registerButton = new Button("Register", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            if (!password.equals(confirmPassword)) {
                Notification.show("Passwords do not match", 3000, Notification.Position.MIDDLE);
            } else {
                // Lógica para llamar al endpoint de registro en tu backend
                Notification.show("User registered successfully", 3000, Notification.Position.MIDDLE);
            }
        });

        formLayout.add(usernameField, passwordField, confirmPasswordField, registerButton);
        add(formLayout);
    }
}
