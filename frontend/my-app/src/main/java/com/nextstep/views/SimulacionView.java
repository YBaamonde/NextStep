package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.nextstep.services.SimulacionService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.NativeLabel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Route("simulacion")
@PageTitle("Simulación | NextStep")
@CssImport("./themes/nextstepfrontend/simulacion-view.css")
public class SimulacionView extends VerticalLayout {

    private final SimulacionService simulacionService = new SimulacionService();
    private final VerticalLayout simulacionContainer;

    public SimulacionView() {
        setClassName("simulacion-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        AuthService authService = new AuthService();
        MainNavbar navbar = new MainNavbar(authService);
        add(navbar);

        simulacionContainer = new VerticalLayout();
        simulacionContainer.setClassName("simulacion-container");
        simulacionContainer.setSpacing(false);
        simulacionContainer.setPadding(false);
        add(simulacionContainer);

        crearSimulacionFormulario();
    }

    private void crearSimulacionFormulario() {
        TextField ingresosField = new TextField("Ingresos");
        ingresosField.setPrefixComponent(new Span("€"));
        ingresosField.setPlaceholder("Ej: 2500");

        NumberField mesesSimulacionField = new NumberField("Meses de Simulación");
        mesesSimulacionField.setPlaceholder("Ej: 6");

        NumberField metaAhorroField = new NumberField("Meta de Ahorro");
        metaAhorroField.setPrefixComponent(new Span("€"));
        metaAhorroField.setPlaceholder("Ej: 1000");

        TextField gastoViviendaField = new TextField("Gasto en Vivienda");
        gastoViviendaField.setPrefixComponent(new Span("€"));
        gastoViviendaField.setPlaceholder("Ej: 800");

        TextField gastoAlimentacionField = new TextField("Gasto en Alimentación");
        gastoAlimentacionField.setPrefixComponent(new Span("€"));
        gastoAlimentacionField.setPlaceholder("Ej: 400");

        TextField gastoTransporteField = new TextField("Gasto en Transporte");
        gastoTransporteField.setPrefixComponent(new Span("€"));
        gastoTransporteField.setPlaceholder("Ej: 200");

        TextField gastoEntretenimientoField = new TextField("Gasto en Entretenimiento");
        gastoEntretenimientoField.setPrefixComponent(new Span("€"));
        gastoEntretenimientoField.setPlaceholder("Ej: 150");

        Button calcularButton = new Button("Calcular Simulación", event -> calcularSimulacion(
                ingresosField.getValue(),
                mesesSimulacionField.getValue(),
                metaAhorroField.getValue(),
                gastoViviendaField.getValue(),
                gastoAlimentacionField.getValue(),
                gastoTransporteField.getValue(),
                gastoEntretenimientoField.getValue()
        ));
        calcularButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        calcularButton.addClassName("calcular-button");

        simulacionContainer.add(ingresosField, mesesSimulacionField, metaAhorroField, gastoViviendaField,
                gastoAlimentacionField, gastoTransporteField, gastoEntretenimientoField, calcularButton);
    }

    private void calcularSimulacion(String ingresos, Double mesesSimulacion, Double metaAhorro,
                                    String gastoVivienda, String gastoAlimentacion, String gastoTransporte,
                                    String gastoEntretenimiento) {

        if (ingresos.isEmpty() || mesesSimulacion == null || metaAhorro == null ||
                gastoVivienda.isEmpty() || gastoAlimentacion.isEmpty() || gastoTransporte.isEmpty() ||
                gastoEntretenimiento.isEmpty()) {
            Notification.show("Todos los campos son obligatorios.");
            return;
        }

        Map<String, Object> simulacionData = Map.of(
                "ingresos", Double.parseDouble(ingresos),
                "mesesSimulacion", mesesSimulacion,
                "metaAhorro", metaAhorro,
                "gastos", Map.of(
                        "vivienda", Double.parseDouble(gastoVivienda),
                        "alimentacion", Double.parseDouble(gastoAlimentacion),
                        "transporte", Double.parseDouble(gastoTransporte),
                        "entretenimiento", Double.parseDouble(gastoEntretenimiento)
                )
        );

        Optional<Map<String, Object>> resultado = simulacionService.calcularSimulacion(simulacionData);

        if (resultado.isPresent()) {
            Map<String, Object> simulacionResultado = resultado.get();
            mostrarResultadoSimulacion(simulacionResultado);
        } else {
            Notification.show("Error al calcular la simulación.");
        }
    }

    private void mostrarResultadoSimulacion(Map<String, Object> simulacionResultado) {
        simulacionContainer.removeAll();

        Double balanceProyectado = (Double) simulacionResultado.get("balanceProyectado");
        List<String> recomendaciones = (List<String>) simulacionResultado.get("recomendaciones");

        NativeLabel balanceLabel = new NativeLabel("Balance Proyectado: €" + balanceProyectado);
        balanceLabel.addClassName("resultado-balance");

        Div recomendacionesDiv = new Div();
        recomendacionesDiv.setClassName("resultado-recomendaciones");
        recomendaciones.forEach(recomendacion -> {
            NativeLabel recomendacionLabel = new NativeLabel(recomendacion);
            recomendacionesDiv.add(recomendacionLabel);
        });

        simulacionContainer.add(balanceLabel, recomendacionesDiv);
    }
}
