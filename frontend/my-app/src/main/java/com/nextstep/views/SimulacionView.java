package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.nextstep.services.SimulacionService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.NativeLabel;

import java.util.*;

@Route("simulacion")
@PageTitle("Simulación | NextStep")
@CssImport("./themes/nextstepfrontend/simulacion-view.css")
public class SimulacionView extends VerticalLayout {

    private final SimulacionService simulacionService = new SimulacionService();
    private final VerticalLayout simulacionContainer;
    private final VerticalLayout resultadosContainer;
    private final Map<String, NumberField> gastosFields = new HashMap<>();
    private final NumberField ingresosField = new NumberField("Ingresos (€)");
    private final NumberField mesesField = new NumberField("Meses de Simulación");
    private final NumberField metaAhorroField = new NumberField("Meta de Ahorro (€)");
    private final Button calcularButton = new Button("Calcular Resultados");
    private final Button exportarPdfButton = new Button("Exportar PDF");
    private final Button nuevaSimulacionButton = new Button("Nueva Simulación");

    public SimulacionView() {
        setClassName("simulacion-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Barra de navegación
        AuthService authService = new AuthService();
        MainNavbar navbar = new MainNavbar(authService);
        add(navbar);

        // Contenedor de simulación
        simulacionContainer = new VerticalLayout();
        simulacionContainer.setClassName("simulacion-container");
        simulacionContainer.setSpacing(false);
        simulacionContainer.setPadding(false);

        addFormFields();
        addSimulacionActions();
        add(simulacionContainer);

        // Contenedor de resultados (inicialmente oculto)
        resultadosContainer = new VerticalLayout();
        resultadosContainer.setClassName("simulacion-container");
        resultadosContainer.setVisible(false);
        add(resultadosContainer);
    }

    private void addFormFields() {
        ingresosField.setPlaceholder("Ej: 2500");
        mesesField.setPlaceholder("Ej: 6");
        metaAhorroField.setPlaceholder("Ej: 1000");

        simulacionContainer.add(ingresosField, mesesField, metaAhorroField);

        // Campos para gastos
        String[] categorias = {"Vivienda", "Alimentación", "Transporte", "Entretenimiento"};
        for (String categoria : categorias) {
            NumberField field = new NumberField(categoria + " (€)");
            field.setPlaceholder("Ej: 500");
            gastosFields.put(categoria.toLowerCase(), field);
            simulacionContainer.add(field);
        }

        // Habilitar botón de calcular solo cuando todos los campos están completos
        calcularButton.setEnabled(false);
        HasValue.ValueChangeListener<? super HasValue.ValueChangeEvent<?>> enableButtonListener = event -> validateInputs();
        ingresosField.addValueChangeListener(enableButtonListener);
        mesesField.addValueChangeListener(enableButtonListener);
        metaAhorroField.addValueChangeListener(enableButtonListener);
        gastosFields.values().forEach(field -> field.addValueChangeListener(enableButtonListener));

    }

    private void addSimulacionActions() {
        calcularButton.setClassName("calcular-button");
        calcularButton.addClickListener(e -> calcularSimulacion());

        simulacionContainer.add(calcularButton);

        exportarPdfButton.setClassName("export-pdf-button");
        exportarPdfButton.addClickListener(e -> exportarPdf());

        nuevaSimulacionButton.setClassName("nueva-simulacion-button");
        nuevaSimulacionButton.addClickListener(e -> crearNuevaSimulacion());
    }

    private void validateInputs() {
        boolean allFieldsFilled = ingresosField.getValue() != null
                && mesesField.getValue() != null
                && metaAhorroField.getValue() != null
                && gastosFields.values().stream().allMatch(field -> field.getValue() != null);

        calcularButton.setEnabled(allFieldsFilled);
    }

    private void calcularSimulacion() {
        Map<String, Object> simulacionData = new HashMap<>();
        simulacionData.put("ingresos", ingresosField.getValue());
        simulacionData.put("mesesSimulacion", mesesField.getValue().intValue());
        simulacionData.put("metaAhorro", metaAhorroField.getValue());

        Map<String, Double> gastos = new HashMap<>();
        gastosFields.forEach((key, field) -> gastos.put(key, field.getValue()));
        simulacionData.put("gastos", gastos);

        simulacionService.calcularSimulacion(simulacionData).ifPresent(this::mostrarResultados);
    }

    private void mostrarResultados(Map<String, Object> resultados) {
        simulacionContainer.setVisible(false);
        resultadosContainer.setVisible(true);
        resultadosContainer.removeAll();

        Double balanceProyectado = (Double) resultados.get("balanceProyectado");
        List<String> recomendaciones = (List<String>) resultados.get("recomendaciones");

        Div balanceDiv = new Div();
        balanceDiv.setText("Balance Proyectado: " + balanceProyectado + " €");
        balanceDiv.setClassName("resultado-balance");
        resultadosContainer.add(balanceDiv);

        Div recomendacionesDiv = new Div();
        recomendacionesDiv.setText("Recomendaciones:");
        recomendacionesDiv.setClassName("resultado-recomendaciones");
        resultadosContainer.add(recomendacionesDiv);

        recomendaciones.forEach(rec -> {
            Div recDiv = new Div();
            recDiv.setText("- " + rec);
            recDiv.setClassName("resultado-recomendaciones");
            resultadosContainer.add(recDiv);
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout(exportarPdfButton, nuevaSimulacionButton);
        buttonsLayout.setClassName("button-layout");
        resultadosContainer.add(buttonsLayout);
    }


    private void exportarPdf() {
        Map<String, Object> simulacionData = new HashMap<>();
        simulacionData.put("ingresos", ingresosField.getValue());
        simulacionData.put("mesesSimulacion", mesesField.getValue().intValue());
        simulacionData.put("metaAhorro", metaAhorroField.getValue());

        Map<String, Double> gastos = new HashMap<>();
        gastosFields.forEach((key, field) -> gastos.put(key, field.getValue()));
        simulacionData.put("gastos", gastos);

        simulacionService.exportarSimulacionPdf(simulacionData);
    }

    private void crearNuevaSimulacion() {
        simulacionContainer.setVisible(true);
        resultadosContainer.setVisible(false);

        ingresosField.clear();
        mesesField.clear();
        metaAhorroField.clear();
        gastosFields.values().forEach(NumberField::clear);

        calcularButton.setEnabled(false);
    }
}
