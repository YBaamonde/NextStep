package com.nextstep.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.nextstep.services.AdminService;

import java.util.List;
import java.util.Map;

@Route("admin-view")
public class AdminView extends VerticalLayout {

    private final AdminService adminService;
    private final Grid<Map<String, Object>> grid = new Grid<>();

    public AdminView(AdminService adminService) {
        this.adminService = adminService;
        setUpGrid();
        loadUsers();
    }

    private void setUpGrid() {
        grid.addColumn(user -> user.get("username")).setHeader("Username");
        grid.addColumn(user -> user.get("email")).setHeader("Email");
        grid.addColumn(user -> user.get("rol")).setHeader("Role");

        grid.addComponentColumn(user -> {
            Button deleteButton = new Button("Eliminar", event -> {
                adminService.deleteUser(Long.valueOf(user.get("id").toString()));
                loadUsers(); // Recargar la lista de usuarios después de eliminar
            });
            return deleteButton;
        });

        add(grid);
    }

    private void loadUsers() {
        List<Map<String, Object>> users = adminService.getUsers();
        if (users != null) {
            grid.setItems(users);
        }
    }
}
