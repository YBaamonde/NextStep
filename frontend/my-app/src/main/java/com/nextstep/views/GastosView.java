package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.nextstep.services.CategoriaService;
import com.nextstep.services.GastoService;
import com.nextstep.services.InAppNotifService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
    private final Map<Integer, VerticalLayout> categoriaRefs = new HashMap<>();




    public GastosView() {
        setClassName("gastos-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        AuthService authService = new AuthService();
        this.categoriaService = new CategoriaService();

        InAppNotifService inAppNotifService = new InAppNotifService();

        // Agregar navbar en la parte superior
        MainNavbar navbar = new MainNavbar(authService, inAppNotifService);
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

            // Crear el panel de categoría y actualizar categoriaRefs
            Div categoryPanel = createCategoryPanel(nombreCategoria, descripcionCategoria, categoriaId, categoriaRefs);

            // Añadir el panel a la vista
            categoriesContainer.add(categoryPanel);
        }
    }





    private void agregarNuevaCategoria() {
        if (categoriaCount >= MAX_CATEGORIES) {
            Notification.show("Has alcanzado el límite de 15 categorías.");
            return;
        }

        Map<String, Object> nuevaCategoria = Map.of("nombre", "Nueva Categoría", "usuarioId", usuarioId);
        Optional<Map<String, Object>> createdCategoria = categoriaService.createCategoria(usuarioId, nuevaCategoria);

        if (createdCategoria.isPresent()) {
            categoriaCount++;
            String nombreCategoria = (String) createdCategoria.get().get("nombre");
            String descripcionCategoria = (String) createdCategoria.get().get("descripcion");
            int categoriaId = (Integer) createdCategoria.get().get("id");

            // Crear el panel de categoría y actualizar categoriaRefs
            Div categoryPanel = createCategoryPanel(nombreCategoria, descripcionCategoria, categoriaId, categoriaRefs);

            // Añadir el nuevo panel al contenedor de categorías
            categoriesContainer.add(categoryPanel);

            Notification.show("Categoría creada con éxito.");
        } else {
            Notification.show("Error al agregar la categoría. Inténtalo nuevamente.");
        }
    }






    private Div createCategoryPanel(String categoryName, String categoryDescription, int categoriaId, Map<Integer, VerticalLayout> categoriaRefs) {
        Div panel = new Div();
        panel.setClassName("panel");

        H2 title = new H2(categoryName);
        title.setClassName("category-title");

        Span description = new Span(categoryDescription != null ? categoryDescription : "Sin descripción");
        description.setClassName("category-description");

        // Contenedor de gastos dentro de la categoría
        VerticalLayout gastosContainer = new VerticalLayout();
        gastosContainer.setClassName("gastos-container");
        gastosContainer.setSpacing(false);
        gastosContainer.setPadding(false);

        // Almacenar el gastosContainer en categoriaRefs
        categoriaRefs.put(categoriaId, gastosContainer);

        Button addGastoButton = new Button("Añadir Gasto");
        addGastoButton.setClassName("gastos-action-button");
        addGastoButton.addClickListener(event -> openAddGastoDialog(categoriaId, gastosContainer));

        // Opciones del menú contextual
        Dialog optionsDialog = new Dialog();
        optionsDialog.setHeaderTitle("Opciones de Categoría");

        Button editButton = new Button("Editar", event -> {
            optionsDialog.close();
            openEditCategoryDialog(categoriaId, title, description);
        });
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button deleteButton = new Button("Eliminar", event -> {
            optionsDialog.close();
            eliminarCategoria(panel, categoriaId);
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttonsLayout = new HorizontalLayout(editButton, deleteButton);
        optionsDialog.add(buttonsLayout);

        Icon menuIcon = new Icon(VaadinIcon.ELLIPSIS_DOTS_V);
        menuIcon.setClassName("context-menu-icon");
        menuIcon.getStyle().set("cursor", "pointer");
        menuIcon.addClickListener(event -> optionsDialog.open());

        // Añadir elementos al panel
        panel.add(title, description, addGastoButton, menuIcon, gastosContainer);

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
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClassName("botones-menu");
        saveButton.getStyle().set("margin-right", "10px");

        Button cancelButton = new Button("Cancelar", event -> editDialog.close());
        cancelButton.addClassName("cancel-button");
        cancelButton.addClassName("botones-menu");

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
        // Configurar para solo fechas pasadas (hoy incluido)
        dateField.setMax(LocalDate.now()); // La fecha máxima es hoy

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

            Optional<Map<String, Object>> result = gastoService.createGasto(usuarioId, categoriaId, gastoData);
            if (result.isPresent()) {
                Map<String, Object> newGasto = result.get();
                Integer newGastoId = (Integer) newGasto.get("id");
                String newNombre = (String) newGasto.get("nombre");
                Double newMonto = (Double) newGasto.get("monto");
                String newFecha = (String) newGasto.get("fecha");

                Div gastoDiv = createGastoDiv(newGastoId, newNombre, newMonto, newFecha);
                gastosContainer.add(gastoDiv);

                addGastoDialog.close();
            } else {
                Notification.show("Error al agregar el gasto. Inténtalo nuevamente.");
            }

        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClassName("botones-menu");

        Button cancelButton = new Button("Cancelar", event -> addGastoDialog.close());
        cancelButton.addClassName("botones-menu");

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        addGastoDialog.add(nameField, amountField, dateField, buttonsLayout);
        addGastoDialog.open();
    }





    // Metodo para actualizar el panel de gastos
    private void cargarGastosPorUsuario(int usuarioId) {
        List<Map<String, Object>> categorias = categoriaService.getCategoriasPorUsuario(usuarioId);

        for (Map<String, Object> categoria : categorias) {
            int categoriaId = (Integer) categoria.get("id");

            // Obtener los últimos 10 gastos de la categoría
            List<Map<String, Object>> gastos = gastoService.getGastosPorCategoriaConLimite(categoriaId, 10);

            VerticalLayout gastosContainer = categoriaRefs.get(categoriaId);
            if (gastosContainer != null) {
                // Limpiar contenedor antes de agregar gastos
                gastosContainer.removeAll();

                // Crear elementos para los gastos
                for (Map<String, Object> gasto : gastos) {
                    Div gastoDiv = createGastoDiv(
                            (Integer) gasto.get("id"),
                            (String) gasto.get("nombre"),
                            (Double) gasto.get("monto"),
                            (String) gasto.get("fecha")
                    );
                    gastosContainer.add(gastoDiv);
                }

                // Agregar botón "Ver más" para mostrar datos históricos
                Button verMasButton = new Button("Ver más", event -> cargarGastosHistoricos(categoriaId, gastosContainer));
                verMasButton.addClassName("ver-mas-button");
                gastosContainer.add(verMasButton);
            }
        }
    }

    // Cargar gastos históricos con el metodo de GastoService
    private void cargarGastosHistoricos(int categoriaId, VerticalLayout gastosContainer) {
        List<Map<String, Object>> gastosHistoricos = gastoService.getGastosHistoricosPorCategoria(categoriaId);

        // Limpiar contenedor antes de agregar gastos
        gastosContainer.removeAll();

        // Crear elementos para los gastos
        for (Map<String, Object> gasto : gastosHistoricos) {
            Div gastoDiv = createGastoDiv(
                    (Integer) gasto.get("id"),
                    (String) gasto.get("nombre"),
                    (Double) gasto.get("monto"),
                    (String) gasto.get("fecha")
            );
            gastosContainer.add(gastoDiv);
        }

        // Agregar botón "Ver menos" para mostrar los últimos 10 gastos
        Button verMenosButton = new Button("Ver menos", event -> cargarGastosPorUsuario(usuarioId));
        verMenosButton.addClassName("ver-menos-button");
        gastosContainer.add(verMenosButton);
    }



    private Div createGastoDiv(Integer gastoId, String nombreGasto, Double montoGasto, String fechaGasto) {
        if (gastoId == null) {
            Notification.show("Error: No se pudo cargar el gasto.");
            return null;
        }

        Div gastoDiv = new Div();
        gastoDiv.setClassName("gasto-item");
        //System.out.println("Creando GastoDiv con gastoId: " + gastoId); // Depuración


        // Etiquetas de información del gasto
        NativeLabel nombreLabel = new NativeLabel("Nombre: " + nombreGasto);
        nombreLabel.addClassName("gasto-nombre");

        NativeLabel montoLabel = new NativeLabel("Monto: " + montoGasto + " €");
        montoLabel.addClassName("gasto-monto");

        NativeLabel fechaLabel = new NativeLabel("Fecha: " + fechaGasto);
        fechaLabel.addClassName("gasto-fecha");

        // Botones de edición y eliminación
        Button editButton = new Button("Editar", event -> openEditGastoDialog(gastoId, gastoDiv, nombreLabel, montoLabel, fechaLabel));
        editButton.addClassName("gastos-action-button");

        Button deleteButton = new Button("Eliminar", event -> eliminarGasto(gastoId, gastoDiv));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClassName("gasto-eliminar");

        // Contenedor para apilar los botones verticalmente
        Div buttonContainer = new Div(editButton, deleteButton);
        buttonContainer.setClassName("gasto-buttons");

        // Añadir las etiquetas y el contenedor de botones al gastoDiv
        gastoDiv.add(nombreLabel, montoLabel, fechaLabel, buttonContainer);

        return gastoDiv;
    }





    private void eliminarGasto(Integer gastoId, Div gastoDiv) {
        boolean success = gastoService.deleteGasto(gastoId);
        if (success) {
            Notification.show("Gasto eliminado con éxito.");

            // Eliminar el Div del contenedor de gastos
            if (gastoDiv.getParent().isPresent()) {
                ((VerticalLayout) gastoDiv.getParent().get()).remove(gastoDiv);
            }
        } else {
            Notification.show("Error al eliminar el gasto.");
        }
    }



    private void openEditGastoDialog(int gastoId, Div gastoDiv, NativeLabel nombreLabel, NativeLabel montoLabel, NativeLabel fechaLabel) {
        Dialog editDialog = new Dialog();
        editDialog.setHeaderTitle("Editar Gasto");
        System.out.println("Abriendo diálogo de edición para gastoId: " + gastoId); // Depuración


        TextField nameField = new TextField("Nombre del Gasto");
        nameField.setValue(nombreLabel.getText().replace("Nombre: ", ""));

        NumberField amountField = new NumberField("Monto");
        amountField.setValue(Double.parseDouble(montoLabel.getText().replace("Monto: ", "").replace(" €", "")));

        DatePicker dateField = new DatePicker("Fecha");
        dateField.setValue(LocalDate.parse(fechaLabel.getText().replace("Fecha: ", "")));
        // Configurar para solo fechas pasadas (hoy incluido)
        dateField.setMax(LocalDate.now()); // La fecha máxima es hoy

        Button saveButton = new Button("Guardar", event -> {
            String nuevoNombre = nameField.getValue();
            Double nuevoMonto = amountField.getValue();
            LocalDate nuevaFecha = dateField.getValue();

            if (nuevoNombre.isEmpty() || nuevoMonto == null || nuevaFecha == null) {
                Notification.show("Todos los campos son obligatorios.");
                return;
            }

            Map<String, Object> updatedGastoData = Map.of(
                    "nombre", nuevoNombre,
                    "monto", nuevoMonto,
                    "fecha", nuevaFecha.toString()
            );

            boolean success = gastoService.updateGasto(gastoId, nuevoNombre, nuevoMonto, nuevaFecha);
            if (success) {
                Notification.show("Gasto actualizado con éxito.");
                editDialog.close();

                // Actualiza los detalles en la interfaz sin refrescar
                nombreLabel.setText("Nombre: " + nuevoNombre);
                montoLabel.setText("Monto: " + nuevoMonto + " €");
                fechaLabel.setText("Fecha: " + nuevaFecha.toString());
            } else {
                Notification.show("Error al actualizar el gasto.");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClassName("botones-menu");

        Button cancelButton = new Button("Cancelar", event -> editDialog.close());
        cancelButton.addClassName("botones-menu");

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        editDialog.add(nameField, amountField, dateField, buttonsLayout);
        editDialog.open();
    }


    /* -------------------- */

}
