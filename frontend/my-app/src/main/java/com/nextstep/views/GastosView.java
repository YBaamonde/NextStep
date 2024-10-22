package com.nextstep.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Gastos - NextStep")
@CssImport("./themes/nextstepfrontend/gastos-view.css")
@Route("gastos")
public class GastosView extends Div {

    public GastosView() {
        addClassName("gastos-view");

        // Panel izquierdo para añadir gastos
        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.addClassName("left-panel");
        Button addGastoButton = new Button("Añadir Gasto");
        addGastoButton.addClassName("add-button");
        leftPanel.add(addGastoButton);

        // Panel central para mostrar la lista de gastos
        VerticalLayout centerPanel = new VerticalLayout();
        centerPanel.addClassName("center-panel");
        Span gastosLabel = new Span("Gastos registrados");
        centerPanel.add(gastosLabel);

        // Ejemplo de lista de gastos
        Div gastosList = new Div();
        gastosList.addClassName("gastos-list");

        // Ejemplo de item de gasto
        Div gastoItem = new Div();
        gastoItem.addClassName("gasto-item");
        Span gastoName = new Span("Transporte: 50€");
        Button editButton = new Button("Editar");
        Button deleteButton = new Button("Eliminar");
        editButton.addClassName("edit-button");
        deleteButton.addClassName("delete-button");

        Div actions = new Div(editButton, deleteButton);
        actions.addClassName("actions");
        gastoItem.add(gastoName, actions);
        gastosList.add(gastoItem);

        centerPanel.add(gastosList);

        // Panel derecho para gestionar categorías
        VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.addClassName("right-panel");
        Span categoriesLabel = new Span("Categorías");
        rightPanel.add(categoriesLabel);

        // Ejemplo de chip de categoría
        Div categoryChip = new Div();
        categoryChip.addClassName("category-chip");
        categoryChip.setText("Alimentación");
        rightPanel.add(categoryChip);

        // Botón para agregar categoría
        Button addCategoryButton = new Button("Agregar Categoría");
        addCategoryButton.addClassName("add-category-button");
        rightPanel.add(addCategoryButton);

        // Añadir los paneles a la vista
        add(leftPanel, centerPanel, rightPanel);
    }
}

