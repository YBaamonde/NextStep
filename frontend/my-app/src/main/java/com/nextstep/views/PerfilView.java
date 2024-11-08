package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.nextstep.services.PerfilService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.ButtonVariant;
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
    private final Integer usuarioId;
    private final TextField usernameField = new TextField();
    private final TextField emailField = new TextField();
    private final AuthService authService = new AuthService();

    public PerfilView() {
        setClassName("perfil-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Cargar la barra de navegación
        MainNavbar navbar = new MainNavbar(authService);
        add(navbar);

        // Obtener el usuario ID de la sesión
        usuarioId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if (usuarioId == null) {
            Notification.show("Error: No se pudo obtener el ID de usuario. Por favor, inicia sesión de nuevo.");
            return;
        }

        // Cargar la información del usuario
        cargarPerfil();

        // Botones de acción
        Button updatePasswordButton = new Button("Actualizar Contraseña", e -> openDialogEditPassword(usuarioId));
        updatePasswordButton.addClassName("action-button");

        Button deleteAccountButton = new Button("Eliminar Cuenta", e -> eliminarCuenta());
        deleteAccountButton.addClassName("action-button");
        deleteAccountButton.addClassName("delete-button");

        Button logoutButton = new Button("Cerrar Sesión", e -> UI.getCurrent().navigate("login"));
        logoutButton.addClassName("logout-button");

        // Panel de perfil
        Div perfilPanel = new Div();
        perfilPanel.addClassName("perfil-panel");
        perfilPanel.add(crearTituloPerfil(), crearFormularioPerfil(), updatePasswordButton, deleteAccountButton, logoutButton);

        add(perfilPanel);
    }

    private H2 crearTituloPerfil() {
        H2 titulo = new H2("Información de Perfil");
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
        editUsernameButton.addClickShortcut(Key.ENTER);

        HorizontalLayout usernameLayout = new HorizontalLayout(usernameField, editUsernameButton);
        usernameLayout.setWidthFull(); // Asegura que ocupe el ancho disponible
        usernameLayout.setAlignItems(Alignment.CENTER); // Alinea verticalmente los elementos al centro

        perfilLayout.add(usernameLayout, emailField);
        return perfilLayout;
    }


    private void cargarPerfil() {
        Optional<Map<String, Object>> perfilOpt = perfilService.getPerfil(usuarioId);
        perfilOpt.ifPresent(perfil -> {
            usernameField.setValue((String) perfil.getOrDefault("username", ""));
            emailField.setValue((String) perfil.getOrDefault("email", ""));
            emailField.setReadOnly(true);
        });
    }

    public void openDialogEditPassword(int usuarioId) {
        Dialog passwordDialog = new Dialog();
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
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Activar el botón solo si el texto coincide con la frase
        confirmField.addValueChangeListener(event -> {
            confirmButton.setEnabled(event.getValue().equals(fraseAleatoria));
        });

        // Botón de cancelación
        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

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
