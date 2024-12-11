package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CssImport("./themes/nextstepfrontend/login-view.css")
@Route("login")
@PageTitle("Login | NextStep")
public class LoginView extends Div {
    private static final Logger logger = LoggerFactory.getLogger(LoginView.class);

    public LoginView() {
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

        // Crear instancia de AuthService
        AuthService authService = new AuthService();

        // Capturar el evento de login y llamar a AuthService
        loginForm.addLoginListener(event -> {
            //System.out.println("Evento de login detectado"); // Debug
            logger.info("Evento de login detectado");

            // Deshabilitar el formulario durante el proceso de autenticación
            loginForm.setEnabled(false);

            // Llamar al metodo de login de AuthService con las credenciales ingresadas
            //AuthService authService = new AuthService();
            authService.login(event.getUsername(), event.getPassword(), success -> {
                if (success) {
                    //System.out.println("Login exitoso, redirigiendo..."); // Debug
                    logger.info("Login exitoso, redirigiendo...");

                    // Redirigir a la página principal si el login es exitoso
                    UI.getCurrent().navigate(InicioView.class);
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
