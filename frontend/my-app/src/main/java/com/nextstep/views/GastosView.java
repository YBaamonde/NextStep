package com.nextstep.views;

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

    public GastosView() {
        setClassName("gastos-container");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Agregar navbar reutilizable
        MainNavbar navbar = new MainNavbar();
        add(navbar);

        // Contenedor de categorías
        FlexLayout categoriesContainer = new FlexLayout();
        categoriesContainer.setClassName("categories-container");
        categoriesContainer.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        categoriesContainer.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // Paneles de categorías (temporalmente con datos de ejemplo)
        for (int i = 1; i <= 3; i++) {
            Div categoryPanel = createCategoryPanel("Categoría " + i);
            categoriesContainer.add(categoryPanel);
        }

        // Botón para agregar una nueva categoría
        Button addCategoryButton = new Button("Agregar Categoría");
        addCategoryButton.setClassName("categoria-button");
        add(addCategoryButton);

        // Agregar contenedor de categorías a la vista
        add(categoriesContainer);
    }

    private Div createCategoryPanel(String categoryName) {
        Div panel = new Div();
        panel.setClassName("panel");

        H2 title = new H2(categoryName);
        title.setClassName("category-title");

        Button addGastoButton = new Button("Añadir Gasto");
        addGastoButton.setClassName("action-button");

        // Menú de contexto (tres puntos)
        Icon menuIcon = new Icon(VaadinIcon.ELLIPSIS_DOTS_V);
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(menuIcon);
        contextMenu.addItem("Editar", event -> {
            // Lógica para editar la categoría
        });
        contextMenu.addItem("Eliminar", event -> {
            // Lógica para eliminar la categoría
        });

        // Añadir elementos al panel
        panel.add(title, addGastoButton, menuIcon);

        return panel;
    }
}
