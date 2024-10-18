package com.nextstep.views;

import com.nextstep.config.SecurityUtils;
import com.nextstep.services.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Route("admin")
public class AdminView extends VerticalLayout {

    private Grid<Map<String, Object>> userGrid = new Grid<>();
    private AuthService authService = new AuthService();

    public AdminView() {
        String token = (String) UI.getCurrent().getSession().getAttribute("authToken");
        if (token == null || !SecurityUtils.hasRole("admin", token)) {
            Notification.show("Acceso denegado. Redirigiendo al login...");
            UI.getCurrent().access(() -> {
                UI.getCurrent().navigate("login");
            });
            return;
        }

        Span adminLabel = new Span("Bienvenido a la vista de administrador");
        Button refreshButton = new Button("Refrescar", e -> refreshUserGrid());
        Button addUserButton = new Button("Agregar Usuario", e -> openCreateUserDialog());

        userGrid.addColumn(user -> user.get("username").toString()).setHeader("Username");
        userGrid.addColumn(user -> user.get("email").toString()).setHeader("Email");
        userGrid.addColumn(user -> user.get("rol").toString()).setHeader("Role");

        userGrid.addComponentColumn(user -> {
            Button deleteButton = new Button("Eliminar", event -> {
                // Obtener el ID del usuario y verificar que no sea nulo
                Object idObject = user.get("id");
                if (idObject == null || !(idObject instanceof Number)) {
                    Notification.show("ID de usuario no encontrado");
                    return;
                }

                Long userId = ((Number) idObject).longValue();
                boolean success = authService.deleteUser(userId);
                if (success) {
                    Notification.show("Usuario eliminado con éxito");
                    refreshUserGrid();
                } else {
                    Notification.show("Error al eliminar el usuario");
                }
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return deleteButton;
        }).setHeader("Acciones");

        refreshUserGrid();

        add(adminLabel, refreshButton, addUserButton, userGrid);
    }

    private void refreshUserGrid() {
        List<Map<String, Object>> users = authService.getAllUsers();
        userGrid.setItems(users);
    }

    private void openCreateUserDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Crear Nuevo Usuario");

        TextField usernameField = new TextField("Username");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        ComboBox<String> roleComboBox = new ComboBox<>("Role");
        roleComboBox.setItems("admin", "normal");

        Button saveButton = new Button("Guardar", event -> {
            boolean success = authService.createUser(
                    usernameField.getValue(),
                    emailField.getValue(),
                    passwordField.getValue(),
                    roleComboBox.getValue()
            );
            if (success) {
                Notification.show("Usuario creado con éxito");
                refreshUserGrid();
                dialog.close();
            } else {
                Notification.show("Error al crear el usuario");
            }
        });

        dialog.add(usernameField, emailField, passwordField, roleComboBox, saveButton);
        dialog.open();
    }
}
