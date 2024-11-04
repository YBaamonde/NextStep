package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.nextstep.services.CategoriaService;
import com.nextstep.services.GastoService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.html.NativeLabel;


import java.time.LocalDate;
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
    private final GastoService gastoService = new GastoService();
    private final Map<Integer, Div> categoriaRefs = new HashMap<>();


    public GastosView() {
        setClassName("gastos-view");
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

        // Cargar los gastos de la categoría
        cargarGastosPorUsuario(usuarioId);

        // Botón para agregar una nueva categoría
        Button addCategoryButton = new Button(new Icon(VaadinIcon.PLUS));
        addCategoryButton.setClassName("categoria-button");
        addCategoryButton.addClickListener(e -> agregarNuevaCategoria());
        add(addCategoryButton);
    }

    /* Metodos para categorías */

    private void cargarCategorias() {
        List<Map<String, Object>> categorias = categoriaService.getCategoriasPorUsuario(usuarioId);
        categoriaCount = categorias.size();

        for (Map<String, Object> categoria : categorias) {
            String nombreCategoria = (String) categoria.get("nombre");
            String descripcionCategoria = (String) categoria.get("descripcion");
            int categoriaId = (Integer) categoria.get("id");

            Div categoryPanel = createCategoryPanel(nombreCategoria, descripcionCategoria, categoriaId, descriptionRefs);
            categoriaRefs.put(categoriaId, categoryPanel);  // Almacena la referencia del panel de la categoría
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

        // Contenedor de gastos dentro de la categoría
        VerticalLayout gastosContainer = new VerticalLayout();
        gastosContainer.setClassName("gastos-container");
        gastosContainer.setSpacing(false);
        gastosContainer.setPadding(false);

        Button addGastoButton = new Button("Añadir Gasto");
        addGastoButton.setClassName("action-button");
        addGastoButton.addClickListener(event -> openAddGastoDialog(categoriaId, gastosContainer));

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
        panel.add(title, description, addGastoButton, menuIcon, gastosContainer);

        // Cargar los gastos de la categoría
        cargarGastosPorUsuario(usuarioId);

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

    /* -------------------- */

    /* Metodos para gastos */

    // Abrir el diálogo de añadir gasto
    private void openAddGastoDialog(int categoriaId, VerticalLayout gastosContainer) {
        Dialog addGastoDialog = new Dialog();
        addGastoDialog.setHeaderTitle("Nuevo Gasto");

        TextField nameField = new TextField("Nombre del Gasto");
        nameField.setPlaceholder("Ej: Transporte");

        NumberField amountField = new NumberField("Monto");
        amountField.setPrefixComponent(new Span("€"));
        amountField.setPlaceholder("Ej: 50.00");

        DatePicker dateField = new DatePicker("Fecha");
        dateField.setPlaceholder("Selecciona una fecha");

        Button saveButton = new Button("Guardar", event -> {
            String nombre = nameField.getValue();
            Double monto = amountField.getValue();
            LocalDate fecha = dateField.getValue();

            if (nombre.isEmpty() || monto == null || fecha == null) {
                Notification.show("Todos los campos son obligatorios.");
                return;
            }

            Map<String, Object> gastoData = Map.of(
                    "nombre", nombre,
                    "monto", monto,
                    "fecha", fecha.toString()
            );

            boolean success = gastoService.createGasto(usuarioId, categoriaId, gastoData);
            if (success) {
                Notification.show("Gasto agregado con éxito.");
                addGastoDialog.close();

                // Crear el nuevo gasto div y añadirlo al panel de la categoría
                Div gastoDiv = createGastoDiv(null, nombre, monto, fecha.toString()); // ID de gasto puede ser null o asignado si se devuelve del servidor
                if (categoriaRefs.containsKey(categoriaId)) {
                    categoriaRefs.get(categoriaId).add(gastoDiv);
                }
            } else {
                Notification.show("Error al agregar el gasto. Inténtalo nuevamente.");
            }
        });

        Button cancelButton = new Button("Cancelar", event -> addGastoDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        addGastoDialog.add(nameField, amountField, dateField, buttonsLayout);
        addGastoDialog.open();
    }

    
    // Metodo para actualizar el panel de gastos
    private void cargarGastosPorUsuario(int usuarioId) {
        List<Map<String, Object>> gastos = gastoService.getGastosPorUsuario(usuarioId);

        for (Map<String, Object> gasto : gastos) {
            Integer gastoId = (Integer) gasto.get("id");
            String nombreGasto = (String) gasto.get("nombre");
            Double montoGasto = (Double) gasto.get("monto");
            String fechaGasto = (String) gasto.get("fecha");

            // Encuentra la categoría asociada al gasto
            Integer categoriaId = (Integer) gasto.get("categoriaId");  // Asegúrate de que los gastos devuelven este campo
            if (categoriaRefs.containsKey(categoriaId)) {
                Div categoriaPanel = categoriaRefs.get(categoriaId);
                // Añade el gasto al panel de la categoría
                Div gastoDiv = createGastoDiv(gastoId, nombreGasto, montoGasto, fechaGasto);
                categoriaPanel.add(gastoDiv);
            } else {
                System.out.println("Categoría no encontrada para gasto con ID: " + gastoId);
            }
        }
    }


    private Div createGastoDiv(Integer gastoId, String nombreGasto, Double montoGasto, String fechaGasto) {
        Div gastoDiv = new Div();
        gastoDiv.setClassName("gasto-item");

        NativeLabel nombreLabel = new NativeLabel("Nombre: " + nombreGasto);
        nombreLabel.addClassName("gasto-nombre");

        NativeLabel montoLabel = new NativeLabel("Monto: " + montoGasto + " €");
        montoLabel.addClassName("gasto-monto");

        NativeLabel fechaLabel = new NativeLabel("Fecha: " + fechaGasto);
        fechaLabel.addClassName("gasto-fecha");

        Button deleteButton = new Button("Eliminar", event -> {
            if (gastoId != null) {
                eliminarGasto(gastoId, gastoDiv);
            } else {
                gastoDiv.getParent().ifPresent(parent -> {
                    if (parent instanceof Div) {
                        ((Div) parent).remove(gastoDiv);
                    }
                });
            }
        });
        deleteButton.addClassName("gasto-eliminar");

        gastoDiv.add(nombreLabel, montoLabel, fechaLabel, deleteButton);

        return gastoDiv;
    }


    private void eliminarGasto(Integer gastoId, Div gastoDiv) {
        boolean success = gastoService.deleteGasto(gastoId);
        if (success) {
            Notification.show("Gasto eliminado con éxito.");
            gastoDiv.getParent().ifPresent(parent -> {
                if (parent instanceof Div) {
                    ((Div) parent).remove(gastoDiv);
                }
            });
        } else {
            Notification.show("Error al eliminar el gasto.");
        }
    }


    /* -------------------- */

}
