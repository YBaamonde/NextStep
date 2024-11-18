package com.nextstep.views.simulacion;

import com.nextstep.services.AuthService;
import com.nextstep.services.SimulacionService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.*;
import java.util.stream.Collectors;

import com.vaadin.flow.component.html.Span;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Route("simulacion")
@PageTitle("Simulación | NextStep")
@CssImport("./themes/nextstepfrontend/simulacion-view.css")
public class SimulacionView extends VerticalLayout {

    private final NumberField ingresosField;
    private final NumberField mesesField;
    private final NumberField metaAhorroField;
    private final Map<String, NumberField> gastosEsencialesFields;
    private final Map<String, NumberField> gastosOpcionalesFields;
    private final Button calcularButton;

    public SimulacionView() {
        setClassName("simulacion-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Navbar
        AuthService authService = new AuthService();
        MainNavbar navbar = new MainNavbar(authService);
        add(navbar);

        // Contenedor principal
        Div simulacionContainer = new Div();
        simulacionContainer.setClassName("simulacion-container");
        add(simulacionContainer);

        // Campo de ingresos
        ingresosField = new NumberField("Ingresos mensuales (€)");
        ingresosField.setPlaceholder("Ej.: 2500");
        ingresosField.setRequiredIndicatorVisible(true);

        // Campo de meses
        mesesField = new NumberField("Duración de la simulación (meses)");
        mesesField.setPlaceholder("Ej.: 6");
        mesesField.setRequiredIndicatorVisible(true);

        // Campo de meta de ahorro
        metaAhorroField = new NumberField("Meta de ahorro (€)");
        metaAhorroField.setPlaceholder("Ej.: 1000");
        metaAhorroField.setRequiredIndicatorVisible(true);

        // Crear campos de gastos esenciales y opcionales
        gastosEsencialesFields = createGastosFields(Map.of(
                "Vivienda", "Ej.: Alquiler, servicios básicos",
                "Compra", "Ej.: Alimentación, limpieza",
                "Transporte", "Ej.: Combustible, transporte público",
                "Salud", "Ej.: Medicamentos, seguro médico",
                "Impuestos", "Ej.: IRPF, IBI, otros"
        ));

        gastosOpcionalesFields = createGastosFields(Map.of(
                "Ocio", "Ej.: Salidas, entretenimiento",
                "Viajes", "Ej.: Vacaciones, escapadas",
                "Compras no esenciales", "Ej.: Ropa, tecnología",
                "Restaurantes", "Ej.: Comidas fuera, comida a domicilio",
                "Suscripciones", "Ej.: Netflix, gimnasio"
        ));

        // Contenedor de categorías
        Div gastosContainer = new Div();
        gastosContainer.setClassName("gastos-container");

        Div esencialesColumn = createCategoryColumn("Gastos Esenciales", gastosEsencialesFields);
        Div opcionalesColumn = createCategoryColumn("Gastos Opcionales", gastosOpcionalesFields);

        gastosContainer.add(esencialesColumn, opcionalesColumn);

        // Botón Calcular
        calcularButton = new Button("Calcular Resultados");
        calcularButton.setEnabled(false);
        calcularButton.addClickListener(event -> calcularSimulacion());
        enableButtonOnValidInputs();

        // Si el botón está activado tiene un estilo diferente
        if (calcularButton.isEnabled()) {
            calcularButton.addClassName("calcular-button");
        }
        else {
            calcularButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }

        // Añadir componentes al contenedor
        simulacionContainer.add(ingresosField, mesesField, metaAhorroField, gastosContainer, calcularButton);
    }

    private Map<String, NumberField> createGastosFields(Map<String, String> placeholders) {
        Map<String, NumberField> fields = new LinkedHashMap<>();
        placeholders.forEach((label, placeholder) -> {
            NumberField field = new NumberField(label);
            field.setPlaceholder(placeholder);
            field.setRequiredIndicatorVisible(true);
            field.addClassName("gasto-field");
            fields.put(label, field);
        });
        return fields;
    }

    private Div createCategoryColumn(String headerText, Map<String, NumberField> fields) {
        Span header = new Span(headerText);
        header.addClassName("category-header");

        Div content = new Div();
        fields.values().forEach(content::add);

        Div column = new Div(header, content);
        return column;
    }

    private void enableButtonOnValidInputs() {
        ingresosField.addValueChangeListener(event -> validateInputs());
        mesesField.addValueChangeListener(event -> validateInputs());
        metaAhorroField.addValueChangeListener(event -> validateInputs());
        gastosEsencialesFields.values().forEach(field -> field.addValueChangeListener(event -> validateInputs()));
        gastosOpcionalesFields.values().forEach(field -> field.addValueChangeListener(event -> validateInputs()));
    }

    private void validateInputs() {
        boolean allInputsValid = ingresosField.getValue() != null
                && mesesField.getValue() != null
                && metaAhorroField.getValue() != null
                && gastosEsencialesFields.values().stream().allMatch(field -> field.getValue() != null)
                && gastosOpcionalesFields.values().stream().allMatch(field -> field.getValue() != null);

        calcularButton.setEnabled(allInputsValid);
    }

    private void calcularSimulacion() {
        try {
            Map<String, Object> simulacionData = new HashMap<>();

            simulacionData.put("ingresos", ingresosField.getValue() != null ? ingresosField.getValue() : 0.0);
            simulacionData.put("mesesSimulacion", mesesField.getValue() != null ? mesesField.getValue().intValue() : 0);
            simulacionData.put("metaAhorro", metaAhorroField.getValue() != null ? metaAhorroField.getValue() : 0.0);

            // Gastos esenciales
            Map<String, Double> gastosEsenciales = gastosEsencialesFields.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getValue()));
            simulacionData.put("gastosEsenciales", gastosEsenciales);

            // Gastos opcionales
            Map<String, Double> gastosOpcionales = gastosOpcionalesFields.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getValue()));
            simulacionData.put("gastosOpcionales", gastosOpcionales);

            // Llamar al servicio para calcular la simulación
            SimulacionService simulacionService = new SimulacionService();
            Optional<Map<String, Object>> result = simulacionService.calcularSimulacion(simulacionData);

            if (result.isPresent()) {
                // Almacenar los datos calculados en la sesión
                UI.getCurrent().getSession().setAttribute("simulacionData", result.get());

                // Navegar a la vista de resultados
                UI.getCurrent().navigate("resultados");
            } else {
                Notification.show("Error al calcular la simulación. Inténtelo nuevamente.");
            }
        } catch (Exception e) {
            Notification.show("Error inesperado. Inténtelo nuevamente.");
            e.printStackTrace();
        }
    }



}
