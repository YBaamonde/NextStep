package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.nextstep.services.InAppNotifService;
import com.nextstep.services.PerfilService;
import com.nextstep.services.NotifConfigService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;

import java.util.Map;
import java.util.Optional;

@Route("perfil")
@PageTitle("Perfil | NextStep")
@CssImport("./themes/nextstepfrontend/perfil-view.css")
public class PerfilView extends VerticalLayout {

    private final PerfilService perfilService = new PerfilService();
    private final NotifConfigService notifConfigService = new NotifConfigService();
    private final Integer usuarioId;
    private final TextField usernameField = new TextField();
    private final TextField emailField = new TextField();
    private final AuthService authService = new AuthService();

    // Componentes de configuración
    private Checkbox emailNotificationsCheckbox;
    private TextField emailDaysBeforeField;
    private Checkbox inAppNotificationsCheckbox;
    private TextField inAppDaysBeforeField;

    public PerfilView() {
        setClassName("perfil-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Obtener el usuario ID de la sesión
        usuarioId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if (usuarioId == null) {
            Notification.show("Error: No se pudo obtener el ID de usuario. Por favor, inicia sesión de nuevo.");
            return;
        }

        // Navbar
        AuthService authService = new AuthService();
        InAppNotifService inAppNotifService = new InAppNotifService();
        MainNavbar navbar = new MainNavbar(authService, inAppNotifService);
        add(navbar);

        // Cargar los paneles
        Div perfilPanel = crearPerfilPanel();
        Div configuracionPanel = crearConfiguracionPanel();

        add(perfilPanel, configuracionPanel);

        // Cargar la información del perfil
        cargarPerfil();
    }

    /* Metodos para el panel de configuración */

    private Div crearConfiguracionPanel() {
        Div configuracionPanel = new Div();
        configuracionPanel.addClassName("config-panel");

        configuracionPanel.add(crearTitulo("Configuración de Notificaciones"), crearFormularioConfiguracion(),
                crearGuardarConfiguracionBoton());

        cargarConfiguracion(); // Cargar configuración inicial desde el backend

        return configuracionPanel;
    }


    private VerticalLayout crearFormularioConfiguracion() {
        VerticalLayout configLayout = new VerticalLayout();
        configLayout.addClassName("perfil-detalles");

        // Notificaciones por Email
        emailNotificationsCheckbox = new Checkbox("Recibir notificaciones por Email");
        emailNotificationsCheckbox.setValue(true); // Activado por defecto
        emailNotificationsCheckbox.addValueChangeListener(e -> emailDaysBeforeField.setEnabled(e.getValue()));

        emailDaysBeforeField = new TextField("Días antes (Email)");
        emailDaysBeforeField.setValue("1"); // Valor por defecto
        emailDaysBeforeField.setEnabled(true);

        // Notificaciones In-App
        inAppNotificationsCheckbox = new Checkbox("Recibir notificaciones In-App");
        inAppNotificationsCheckbox.setValue(true); // Activado por defecto
        inAppNotificationsCheckbox.addValueChangeListener(e -> inAppDaysBeforeField.setEnabled(e.getValue()));

        inAppDaysBeforeField = new TextField("Días antes (In-App)");
        inAppDaysBeforeField.setValue("1"); // Valor por defecto
        inAppDaysBeforeField.setEnabled(true);

        // Llama a configurarListeners para agregar las validaciones
        configurarListeners();

        configLayout.add(emailNotificationsCheckbox, emailDaysBeforeField, inAppNotificationsCheckbox, inAppDaysBeforeField);

        return configLayout;
    }




    // Metodo para cargar la configuración inicial desde el backend
    private void cargarConfiguracion() {
        Optional<Map<String, Object>> configOpt = notifConfigService.obtenerConfiguracion(usuarioId);

        configOpt.ifPresent(config -> {
            emailNotificationsCheckbox.setValue(config.containsKey("emailActivas")
                    ? (Boolean) config.get("emailActivas")
                    : true);
            emailDaysBeforeField.setValue(config.containsKey("emailDiasAntes")
                    ? String.valueOf(config.get("emailDiasAntes"))
                    : "1");
            inAppNotificationsCheckbox.setValue(config.containsKey("inAppActivas")
                    ? (Boolean) config.get("inAppActivas")
                    : true);
            inAppDaysBeforeField.setValue(config.containsKey("inAppDiasAntes")
                    ? String.valueOf(config.get("inAppDiasAntes"))
                    : "1");
        });
    }

    private void configurarListeners() {
        emailDaysBeforeField.addValueChangeListener(e -> {
            String value = e.getValue();
            if (value.isEmpty() || Integer.parseInt(value) < 1) {
                emailDaysBeforeField.setValue("1");
                Notification.show("El valor mínimo permitido es 1.");
            }
        });

        inAppDaysBeforeField.addValueChangeListener(e -> {
            String value = e.getValue();
            if (value.isEmpty() || Integer.parseInt(value) < 1) {
                inAppDaysBeforeField.setValue("1");
                Notification.show("El valor mínimo permitido es 1.");
            }
        });
    }


    private void guardarConfiguracion() {
        Map<String, Object> config = Map.of(
                "usuario", Map.of("id", usuarioId), // Incluir el objeto usuario con su ID
                "emailActivas", emailNotificationsCheckbox.getValue(),
                "emailDiasAntes", Integer.parseInt(emailDaysBeforeField.getValue()),
                "inAppActivas", inAppNotificationsCheckbox.getValue(),
                "inAppDiasAntes", Integer.parseInt(inAppDaysBeforeField.getValue())
        );

        boolean success = notifConfigService.guardarConfiguracion(config);
        if (success) {
            Notification.show("Configuración guardada correctamente.");
        } else {
            Notification.show("Error al guardar la configuración.");
        }
    }


    private Button crearGuardarConfiguracionBoton() {
        Button saveConfigButton = new Button("Guardar Configuración", e -> guardarConfiguracion());
        saveConfigButton.addClassName("action-button");
        return saveConfigButton;
    }



    /* Metodos para el panel de perfil */

    private void cargarPerfil() {
        if (usuarioId == null) {
            Notification.show("Error: Usuario no autenticado. Por favor, inicie sesión nuevamente.");
            return;
        }

        // Obtener los datos del perfil desde el servicio
        Optional<Map<String, Object>> perfilOpt = perfilService.getPerfil(usuarioId);

        if (perfilOpt.isPresent()) {
            Map<String, Object> perfil = perfilOpt.get();
            usernameField.setValue((String) perfil.getOrDefault("username", ""));
            emailField.setValue((String) perfil.getOrDefault("email", ""));
        } else {
            Notification.show("Error al cargar el perfil. Por favor, intente nuevamente.", 3000, Notification.Position.MIDDLE);
        }
    }


    private Div crearPerfilPanel() {
        Div perfilPanel = new Div();
        perfilPanel.addClassName("perfil-panel");

        perfilPanel.add(crearTitulo("Información de Perfil"), crearFormularioPerfil(),
                crearActualizarContrasenaBoton(), crearEliminarCuentaBoton(), crearCerrarSesionBoton());

        return perfilPanel;
    }


    private H2 crearTitulo(String texto) {
        H2 titulo = new H2(texto);
        titulo.addClassName("perfil-titulo");
        return titulo;
    }

    private VerticalLayout crearFormularioPerfil() {
        VerticalLayout perfilLayout = new VerticalLayout();
        perfilLayout.addClassName("perfil-detalles");

        usernameField.setLabel("Nombre de Usuario");
        usernameField.setReadOnly(true);
        usernameField.addClassName("perfil-input");

        Button editUsernameButton = new Button("Editar", e -> openDialogEditUsername());
        editUsernameButton.addClassName("edit-button");

        HorizontalLayout usernameLayout = new HorizontalLayout(usernameField, editUsernameButton);
        usernameLayout.setWidthFull();
        usernameLayout.setAlignItems(Alignment.CENTER);

        perfilLayout.add(usernameLayout, emailField);

        emailField.setLabel("Correo Electrónico");
        emailField.setReadOnly(true);
        emailField.addClassName("perfil-input");

        return perfilLayout;
    }


    private Button crearActualizarContrasenaBoton() {
        Button updatePasswordButton = new Button("Actualizar Contraseña", e -> openDialogEditPassword(usuarioId));
        updatePasswordButton.addClassName("action-button");
        return updatePasswordButton;
    }

    private Button crearEliminarCuentaBoton() {
        Button deleteAccountButton = new Button("Eliminar Cuenta", e -> eliminarCuenta());
        deleteAccountButton.addClassName("action-button");
        deleteAccountButton.addClassName("delete-button");
        return deleteAccountButton;
    }

    private Button crearCerrarSesionBoton() {
        Button logoutButton = new Button("Cerrar Sesión", e -> UI.getCurrent().navigate("login"));
        logoutButton.addClassName("logout-button");
        return logoutButton;
    }


    public void openDialogEditPassword(int usuarioId) {
        Dialog passwordDialog = new Dialog();
        passwordDialog.addClassName("perfil-dialog");
        passwordDialog.setWidth("500px");
        passwordDialog.setMinWidth("300px");
        passwordDialog.setModal(true);
        passwordDialog.setDraggable(true);

        PasswordField currentPasswordField = new PasswordField("Contraseña Actual");
        currentPasswordField.setWidthFull();

        PasswordField newPasswordField = new PasswordField("Nueva Contraseña");
        newPasswordField.setWidthFull();
        newPasswordField.setEnabled(false);

        Button validateButton = new Button("Validar Contraseña", event -> {
            String currentPassword = currentPasswordField.getValue();
            String username = (String) UI.getCurrent().getSession().getAttribute("username");

            authService.validatePassword(username, currentPassword, isValid -> {
                if (isValid) {
                    newPasswordField.setEnabled(true);
                    Notification.show("Contraseña actual verificada. Puedes ingresar una nueva contraseña.");
                } else {
                    Notification.show("Contraseña actual incorrecta.", 3000, Notification.Position.MIDDLE);
                }
            });
        });
        validateButton.addClassName("action-button");

        Button saveButton = new Button("Guardar", event -> {
            String newPassword = newPasswordField.getValue();
            if (newPassword.isEmpty()) {
                Notification.show("Por favor, ingresa una nueva contraseña.");
                return;
            }

            boolean success = perfilService.updatePassword(usuarioId, newPassword);
            if (success) {
                Notification.show("Contraseña actualizada exitosamente.");
                passwordDialog.close();
            } else {
                Notification.show("Error al actualizar la contraseña.");
            }
        });
        saveButton.addClassName("edit-buttons");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> passwordDialog.close());
        cancelButton.addClassName("edit-buttons");

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(currentPasswordField, validateButton, newPasswordField, buttonsLayout);
        passwordDialog.add(dialogLayout);

        passwordDialog.open();
    }

    // Metodo para elegir una frase de eliminación de cuenta
    private String generarFraseAleatoria() {
        String[] frases = {
                "Confirmo que quiero eliminar mi cuenta",
                "Eliminar cuenta permanentemente",
                "Quiero borrar todos mis datos",
                "Confirmo la eliminación de mi cuenta",
                "Deseo cerrar mi cuenta de forma definitiva"
        };
        int index = (int) (Math.random() * frases.length);
        return frases[index];
    }


    private void eliminarCuenta() {
        // Generar la frase aleatoria
        String fraseAleatoria = generarFraseAleatoria();

        // Crear el diálogo de confirmación
        Dialog confirmDialog = new Dialog();
        confirmDialog.addClassName("perfil-dialog");
        confirmDialog.setWidth("400px");
        confirmDialog.setModal(true);
        confirmDialog.setDraggable(false);

        // Título del diálogo
        H3 dialogTitle = new H3("Confirmación de Eliminación de Cuenta");
        dialogTitle.getStyle().set("text-align", "center");

        // Instrucciones y frase de confirmación
        Span instructions = new Span("Para eliminar tu cuenta, escribe la siguiente frase de confirmación:");
        instructions.getStyle().set("font-size", "1em");

        Span fraseConfirmacion = new Span("\"" + fraseAleatoria + "\"");
        fraseConfirmacion.getStyle().set("font-weight", "bold");
        fraseConfirmacion.getStyle().set("display", "block");
        fraseConfirmacion.getStyle().set("text-align", "center");
        fraseConfirmacion.getStyle().set("margin", "10px 0");

        // Campo de texto donde el usuario debe escribir la frase
        TextField confirmField = new TextField();
        confirmField.setWidthFull();
        confirmField.setPlaceholder("Escribe la frase exacta aquí");

        // Botón de confirmación (deshabilitado inicialmente)
        Button confirmButton = new Button("Confirmar Eliminación", event -> {
            // Llamar a perfilService para eliminar el usuario si la frase es correcta
            boolean success = perfilService.deleteUsuario(usuarioId);
            if (success) {
                Notification.show("Cuenta eliminada con éxito.");
                UI.getCurrent().navigate("login");
            } else {
                Notification.show("Error al eliminar la cuenta.");
            }
            confirmDialog.close();
        });
        confirmButton.setEnabled(false);

        // Activar el botón solo si el texto coincide con la frase
        confirmField.addValueChangeListener(event -> {
            confirmButton.setEnabled(event.getValue().equals(fraseAleatoria));
            confirmButton.addClassName("confirm-delete-button");
        });

        // Botón de cancelación
        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());
        //cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Añadir los componentes al diálogo
        VerticalLayout dialogLayout = new VerticalLayout(dialogTitle, instructions, fraseConfirmacion, confirmField, new HorizontalLayout(confirmButton, cancelButton));
        dialogLayout.setAlignItems(Alignment.CENTER);
        confirmDialog.add(dialogLayout);

        // Abrir el diálogo
        confirmDialog.open();
    }


    // Metodo para abrir el diálogo de edición de nombre de usuario
    private void openDialogEditUsername() {
        Dialog usernameDialog = new Dialog();
        usernameDialog.addClassName("perfil-dialog");
        usernameDialog.setWidth("400px");
        usernameDialog.setMinWidth("300px");

        TextField newUsernameField = new TextField("Nuevo Nombre de Usuario");
        newUsernameField.setWidthFull();

        Button saveButton = new Button("Guardar", event -> {
            String newUsername = newUsernameField.getValue();
            if (newUsername.isEmpty()) {
                Notification.show("Por favor, ingresa un nombre de usuario.");
                return;
            }

            // Llamada a PerfilService para actualizar el nombre de usuario
            boolean success = perfilService.updateUsername(usuarioId, newUsername);
            if (success) {
                Notification.show("Nombre de usuario actualizado exitosamente.");
                usernameField.setValue(newUsername); // Actualizar campo en la vista
                usernameDialog.close();
            } else {
                Notification.show("Error al actualizar el nombre de usuario.");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> usernameDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(newUsernameField, buttonsLayout);
        usernameDialog.add(dialogLayout);

        usernameDialog.open();
    }
}
