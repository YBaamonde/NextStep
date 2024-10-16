package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.nextstep.views.helloworld.HelloWorldView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./themes/nextstepfrontend/login-view.css")
@Route("login")
public class LoginView extends Div {

    private final AuthService authService;

    public LoginView(AuthService authService) {  // Inyectar AuthService
        this.authService = authService;

        addClassName("login-view");

        // Crear un layout para centrar el formulario
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.addClassName("login-container");

        // Botón para registrarse
        Button registerButton = new Button("Registrarse", event -> {
            // Redirige al usuario a la vista de registro
            getUI().ifPresent(ui -> ui.navigate("register"));
        });

        // Estilo del botón de registro
        registerButton.addClassName("register-link");

        // Formulario de login
        LoginForm loginForm = new LoginForm();
        loginForm.getElement().getThemeList().add("light");

        // Configurar el texto internacionalizado
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Inicio de sesión");
        i18n.getForm().setUsername("Username or Email");
        i18n.getForm().setSubmit("Iniciar sesión");
        i18n.getForm().setForgotPassword("Olvidé mi contraseña");
        i18n.getErrorMessage().setTitle("Credenciales inválidas");
        i18n.getErrorMessage().setMessage("Por favor, verifica tu email y contraseña e intenta nuevamente.");
        loginForm.setI18n(i18n);

        // Capturar el evento de login y llamar a AuthService
        loginForm.addLoginListener(event -> {
            // Deshabilitar el formulario durante el proceso de autenticación
            loginForm.setEnabled(false);

            authService.login(event.getUsername(), event.getPassword(), success -> {
                if (success) {
                    // Redirigir a la página principal si el login es exitoso
                    UI.getCurrent().navigate(HelloWorldView.class);
                } else {
                    // Mostrar una notificación de error
                    Notification.show("Error en el inicio de sesión. Por favor, revisa tus credenciales.");
                    // Habilitar el formulario nuevamente para que el usuario pueda volver a intentarlo
                    loginForm.setEnabled(true);
                }
            });
        });

        layout.add(loginForm, registerButton);
        add(layout);
    }
}
