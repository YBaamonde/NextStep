package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.nextstep.services.CategoriaService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.util.List;
import java.util.Map;

@Route("gastos")
@PageTitle("Gastos | NextStep")
@CssImport("./themes/nextstepfrontend/gastos-view.css")
public class GastosView extends VerticalLayout {

    private static final int MAX_CATEGORIES = 15;
    private final CategoriaService categoriaService;
    private final FlexLayout categoriesContainer;
    private final Integer usuarioId;
    private int categoriaCount;

    public GastosView() {
        setClassName("gastos-container");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        AuthService authService = new AuthService();
        this.categoriaService = new CategoriaService();

        // Agregar navbar en la parte superior
        MainNavbar navbar = new MainNavbar(authService);
        add(navbar);

        // Inicializar el contenedor de categorías
        categoriesContainer = new FlexLayout();
        categoriesContainer.setClassName("categories-container");
        categoriesContainer.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        categoriesContainer.setJustifyContentMode(JustifyContentMode.BETWEEN);
        add(categoriesContainer); // Agregarlo después de la navbar

        // Obtener el userId del usuario actual desde la sesión
        usuarioId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if (usuarioId == null) {
            Notification.show("Error: No se pudo obtener el ID de usuario. Por favor, inicia sesión de nuevo.");
            return;
        }

        // Cargar categorías existentes desde la base de datos
        cargarCategorias();

        // Botón para agregar una nueva categoría
        Button addCategoryButton = new Button("Agregar Categoría");
        addCategoryButton.setClassName("categoria-button");
        addCategoryButton.addClickListener(e -> agregarNuevaCategoria());
        add(addCategoryButton);
    }

    private void cargarCategorias() {
        List<Map<String, Object>> categorias = categoriaService.getCategoriasPorUsuario(usuarioId);
        categoriaCount = categorias.size();

        for (Map<String, Object> categoria : categorias) {
            String nombreCategoria = (String) categoria.get("nombre");
            int categoriaId = (Integer) categoria.get("id");
            Div categoryPanel = createCategoryPanel(nombreCategoria, categoriaId);
            categoriesContainer.add(categoryPanel);
        }
    }

    private void agregarNuevaCategoria() {
        if (categoriaCount >= MAX_CATEGORIES) {
            Notification.show("Has alcanzado el límite de 15 categorías.");
            return;
        }

        // Crear el objeto de la nueva categoría
        Map<String, Object> nuevaCategoria = Map.of("nombre", "Nueva Categoría", "usuarioId", usuarioId);
        boolean success = categoriaService.createCategoria(usuarioId, nuevaCategoria);

        if (success) {
            categoriaCount++;
            Div categoryPanel = createCategoryPanel("Nueva Categoría", categoriaCount);
            categoriesContainer.add(categoryPanel);
        } else {
            Notification.show("Error al agregar la categoría. Inténtalo nuevamente.");
        }
    }

    private Div createCategoryPanel(String categoryName, int categoriaId) {
        Div panel = new Div();
        panel.setClassName("panel");

        H2 title = new H2(categoryName);
        title.setClassName("category-title");

        Button addGastoButton = new Button("Añadir Gasto");
        addGastoButton.setClassName("action-button");

        // Menú de contexto (tres puntos) para editar y eliminar
        Icon menuIcon = new Icon(VaadinIcon.ELLIPSIS_DOTS_V);
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(menuIcon);

        // Configura el menú para abrirse con un click izquierdo
        contextMenu.setOpenOnClick(true);
        // El estilo del puntero del ratón cambiará cuando se pase el ratón por encima del menú
        menuIcon.setClassName("context-menu");

        // Opción de editar categoría
        contextMenu.addItem("Editar", event -> openEditCategoryDialog(categoryName, categoriaId, title));

        // Opción de eliminar categoría
        contextMenu.addItem("Eliminar", event -> eliminarCategoria(panel, categoriaId));

        // Añadir elementos al panel
        panel.add(title, addGastoButton, menuIcon);
        return panel;
    }


    private void openEditCategoryDialog(String categoryName, int categoriaId, H2 title) {
        Dialog editDialog = new Dialog();
        editDialog.setHeaderTitle("Editar Categoría");

        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Nombre de la Categoría");
        nameField.setValue(categoryName);

        TextArea descriptionField = new TextArea("Descripción");
        descriptionField.setValue("Descripción actual...");

        formLayout.add(nameField, descriptionField);

        Button saveButton = new Button("Guardar", event -> {
            String newName = nameField.getValue();
            String newDescription = descriptionField.getValue();
            boolean success = categoriaService.updateCategoria(categoriaId, newName, newDescription);

            if (success) {
                Notification.show("Categoría actualizada exitosamente");
                title.setText(newName);  // Actualiza el título en el panel
                editDialog.close();
            } else {
                Notification.show("Error al actualizar la categoría");
            }
        });

        Button cancelButton = new Button("Cancelar", event -> editDialog.close());
        editDialog.getFooter().add(cancelButton, saveButton);
        editDialog.add(formLayout);
        editDialog.open();
    }


    private void eliminarCategoria(Div panel, int categoriaId) {
        boolean success = categoriaService.deleteCategoria(categoriaId);

        if (success) {
            categoriesContainer.remove(panel);
            categoriaCount--;
            Notification.show("Categoría eliminada exitosamente.");
        } else {
            Notification.show("Error al eliminar la categoría.");
        }
    }
}
