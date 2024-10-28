package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("gastos")
@PageTitle("Gastos | NextStep")
@CssImport("./themes/nextstepfrontend/gastos-view.css")
public class GastosView extends VerticalLayout {

    private final AuthService authService; // Agregar AuthService como dependencia
    private final FlexLayout categoriesContainer; // Contenedor de categorías

    public GastosView() {
        setClassName("gastos-container");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        this.authService = new AuthService();

        // Agregar navbar reutilizable
        MainNavbar navbar = new MainNavbar(authService);
        add(navbar);

        // Contenedor de categorías
        categoriesContainer = new FlexLayout();
        categoriesContainer.setClassName("categories-container");
        categoriesContainer.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        categoriesContainer.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // Agregar contenedor de categorías a la vista
        add(categoriesContainer);

        // Botón para agregar una nueva categoría
        Button addCategoryButton = new Button("Agregar Categoría");
        addCategoryButton.setClassName("categoria-button");
        addCategoryButton.addClickListener(e -> addNewCategoryPanel());

        // Agregar el botón de agregar categoría al final de la vista
        add(addCategoryButton);
    }

    // Metodo para crear y agregar un nuevo panel de categoría
    private void addNewCategoryPanel() {
        String categoryName = "Categoría " + (categoriesContainer.getComponentCount() + 1);
        Div categoryPanel = createCategoryPanel(categoryName);
        categoriesContainer.add(categoryPanel);
    }

    private Div createCategoryPanel(String categoryName) {
        Div panel = new Div();
        panel.setClassName("panel");

        // Título de la categoría
        H2 title = new H2(categoryName);
        title.setClassName("category-title");

        // Botón para agregar gasto
        Button addGastoButton = new Button("Añadir Gasto");
        addGastoButton.setClassName("action-button");

        // Menú de contexto (tres puntos) para editar y eliminar
        Icon menuIcon = new Icon(VaadinIcon.ELLIPSIS_DOTS_V);
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(menuIcon);
        contextMenu.addItem("Editar", event -> {
            // Lógica para editar la categoría
        });
        contextMenu.addItem("Eliminar", event -> {
            // Lógica para eliminar la categoría
            categoriesContainer.remove(panel);
        });

        // Añadir elementos al panel de categoría
        panel.add(title, addGastoButton, menuIcon);

        return panel;
    }
}
