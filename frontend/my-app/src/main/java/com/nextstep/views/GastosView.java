package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.nextstep.services.CategoriaService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Route("gastos")
@PageTitle("Gastos | NextStep")
@CssImport("./themes/nextstepfrontend/gastos-view.css")
public class GastosView extends VerticalLayout {

    private static final int MAX_CATEGORIES = 15;
    private final CategoriaService categoriaService;
    private final FlexLayout categoriesContainer;
    private final Integer usuarioId;
    private int categoriaCount;
    private final Div spacer;
    private final Map<Integer, Span> descriptionRefs = new HashMap<>();


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

        // Crear el espaciador y añadirlo al contenedor
        spacer = new Div();
        spacer.addClassName("category-spacer");
        categoriesContainer.add(spacer);

        // Obtener el userId del usuario actual desde la sesión
        usuarioId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if (usuarioId == null) {
            Notification.show("Error: No se pudo obtener el ID de usuario. Por favor, inicia sesión de nuevo.");
            return;
        }


        // Cargar categorías existentes desde la base de datos
        cargarCategorias();

        // Botón para agregar una nueva categoría
        Button addCategoryButton = new Button(new Icon(VaadinIcon.PLUS));
        addCategoryButton.setClassName("categoria-button");
        addCategoryButton.addClickListener(e -> agregarNuevaCategoria());
        add(addCategoryButton);
    }

    private void cargarCategorias() {
        List<Map<String, Object>> categorias = categoriaService.getCategoriasPorUsuario(usuarioId);
        categoriaCount = categorias.size();

        for (Map<String, Object> categoria : categorias) {
            String nombreCategoria = (String) categoria.get("nombre");
            String descripcionCategoria = (String) categoria.get("descripcion");
            int categoriaId = (Integer) categoria.get("id");

            // Crear el panel de categoría con el mapa descriptionRefs
            Div categoryPanel = createCategoryPanel(nombreCategoria, descripcionCategoria, categoriaId, descriptionRefs);

            // Añadir la nueva categoría antes del espaciador
            categoriesContainer.addComponentAtIndex(categoriesContainer.getComponentCount() - 1, categoryPanel);
        }
    }





    private void agregarNuevaCategoria() {
        if (categoriaCount >= MAX_CATEGORIES) {
            Notification.show("Has alcanzado el límite de 15 categorías.");
            return;
        }

        // Crear el objeto de la nueva categoría
        Map<String, Object> nuevaCategoria = Map.of("nombre", "Nueva Categoría", "usuarioId", usuarioId);
        Optional<Map<String, Object>> createdCategoria = categoriaService.createCategoria(usuarioId, nuevaCategoria);

        if (createdCategoria.isPresent()) {
            categoriaCount++;
            String nombreCategoria = (String) createdCategoria.get().get("nombre");
            String descripcionCategoria = (String) createdCategoria.get().get("descripcion");
            int categoriaId = (Integer) createdCategoria.get().get("id");

            // Crear el panel de la nueva categoría y pasar descriptionRefs
            Div categoryPanel = createCategoryPanel(nombreCategoria, descripcionCategoria, categoriaId, descriptionRefs);

            // Agregar el nuevo panel al final de categoriesContainer
            categoriesContainer.addComponentAtIndex(categoriesContainer.getComponentCount() - 1, categoryPanel);

            Notification.show("Categoría creada con éxito.");
        } else {
            Notification.show("Error al agregar la categoría. Inténtalo nuevamente.");
        }
    }


    private Div createCategoryPanel(String categoryName, String categoryDescription, int categoriaId, Map<Integer, Span> descriptionRefs) {
        Div panel = new Div();
        panel.setClassName("panel");

        H2 title = new H2(categoryName);
        title.setClassName("category-title");

        Span description = new Span(categoryDescription != null ? categoryDescription : "Sin descripción");
        description.setClassName("category-description");
        descriptionRefs.put(categoriaId, description);

        Button addGastoButton = new Button("Añadir Gasto");
        addGastoButton.setClassName("action-button");

        // Crear el diálogo de opciones de categoría
        Dialog optionsDialog = new Dialog();
        optionsDialog.setHeaderTitle("Opciones de Categoría");

        // Botón para editar
        Button editButton = new Button("Editar", event -> {
            optionsDialog.close();
            openEditCategoryDialog(categoriaId, title, description);
        });
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Botón para eliminar
        Button deleteButton = new Button("Eliminar", event -> {
            optionsDialog.close();
            eliminarCategoria(panel, categoriaId);
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Añadir botones al diálogo
        HorizontalLayout buttonsLayout = new HorizontalLayout(editButton, deleteButton);
        optionsDialog.add(buttonsLayout);

        // Icono de menú para abrir el diálogo
        Icon menuIcon = new Icon(VaadinIcon.ELLIPSIS_DOTS_V);
        menuIcon.setClassName("context-menu-icon");
        menuIcon.getStyle().set("cursor", "pointer");
        menuIcon.addClickListener(event -> optionsDialog.open());

        // Añadir elementos al panel
        panel.add(title, description, addGastoButton, menuIcon);
        return panel;
    }







    private void openEditCategoryDialog(int categoriaId, H2 title, Span description) {
        Dialog editDialog = new Dialog();
        editDialog.setHeaderTitle("Editar Categoría");

        TextField nameField = new TextField("Nombre");
        nameField.setValue(title.getText());

        TextArea descriptionField = new TextArea("Descripción");
        descriptionField.setValue(description.getText());

        // Crear botones de guardar y cancelar
        Button saveButton = new Button("Guardar", event -> {
            String newName = nameField.getValue();
            String newDescription = descriptionField.getValue();

            boolean success = categoriaService.updateCategoria(categoriaId, newName, newDescription);
            if (success) {
                Notification.show("Categoría actualizada con éxito.");
                editDialog.close();

                // Actualizar los textos directamente en el panel
                title.setText(newName);
                description.setText(newDescription);
            } else {
                Notification.show("Error al actualizar la categoría.");
            }
        });
        saveButton.getStyle().set("margin-right", "10px");

        Button cancelButton = new Button("Cancelar", event -> editDialog.close());
        cancelButton.addClassName("cancel-button");

        // Crear un layout para los botones y añadirles algo de espacio
        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);

        // Añadir en un layout vertical dentro del diálogo
        VerticalLayout dialogLayout = new VerticalLayout(nameField, descriptionField, buttonsLayout);
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        editDialog.add(dialogLayout);
        editDialog.open();
    }


    private void eliminarCategoria(Div panel, int categoriaId) {
        boolean success = categoriaService.deleteCategoria(categoriaId);

        if (success) {
            categoriesContainer.remove(panel);
            categoriaCount--;
            Notification.show("Categoría eliminada con éxito.");
        } else {
            Notification.show("Error al eliminar la categoría.");
        }
    }




}
