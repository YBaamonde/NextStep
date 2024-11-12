package com.nextstep.views;

import com.nextstep.services.AuthService;
import com.nextstep.services.PagoService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
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

@Route("pagos")
@PageTitle("Pagos | NextStep")
@CssImport("./themes/nextstepfrontend/pagos-view.css")
public class PagosView extends VerticalLayout {

    private final PagoService pagoService = new PagoService();
    private final Integer usuarioId;
    private final VerticalLayout pagosContainer;

    public PagosView() {
        setClassName("pagos-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        AuthService authService = new AuthService();
        MainNavbar navbar = new MainNavbar(authService);
        add(navbar);

        pagosContainer = new VerticalLayout();
        pagosContainer.setClassName("pagos-container");
        pagosContainer.setSpacing(false);
        pagosContainer.setPadding(false);
        add(pagosContainer);

        usuarioId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if (usuarioId == null) {
            Notification.show("Error: No se pudo obtener el ID de usuario. Por favor, inicia sesión de nuevo.");
            return;
        }

        cargarPagosPorUsuario(usuarioId);

        Button addPagoButton = new Button("Añadir Pago", new Icon(VaadinIcon.PLUS));
        addPagoButton.setClassName("pago-button");
        addPagoButton.addClickListener(e -> openAddPagoDialog());
        add(addPagoButton);
    }

    private void cargarPagosPorUsuario(int usuarioId) {
        List<Map<String, Object>> pagos = pagoService.getPagosPorUsuario(usuarioId);

        for (Map<String, Object> pago : pagos) {
            Integer pagoId = (Integer) pago.get("id");
            String nombrePago = (String) pago.get("nombre");
            Double montoPago = (Double) pago.get("monto");
            String fechaPago = (String) pago.get("fecha");
            Boolean recurrente = (Boolean) pago.get("recurrente");
            String frecuencia = (String) pago.get("frecuencia");

            Div pagoDiv = createPagoDiv(pagoId, nombrePago, montoPago, fechaPago, recurrente, frecuencia);
            pagosContainer.add(pagoDiv);
        }
    }

    private Div createPagoDiv(Integer pagoId, String nombre, Double monto, String fecha, Boolean recurrente, String frecuencia) {
        if (pagoId == null) {
            Notification.show("Error: No se pudo cargar el pago.");
            return null;
        }

        Div pagoDiv = new Div();
        pagoDiv.setClassName("pago-item");

        NativeLabel nombreLabel = new NativeLabel("Nombre: " + nombre);
        nombreLabel.addClassName("pago-nombre");

        NativeLabel montoLabel = new NativeLabel("Monto: " + monto + " €");
        montoLabel.addClassName("pago-monto");

        NativeLabel fechaLabel = new NativeLabel("Fecha: " + fecha);
        fechaLabel.addClassName("pago-fecha");

        NativeLabel recurrenteLabel = new NativeLabel("Recurrente: " + (recurrente ? "Sí" : "No"));
        recurrenteLabel.addClassName("pago-recurrente");

        NativeLabel frecuenciaLabel = new NativeLabel("Frecuencia: " + (frecuencia != null ? frecuencia : "N/A"));
        frecuenciaLabel.addClassName("pago-frecuencia");

        Button editButton = new Button("Editar", event -> openEditPagoDialog(pagoId, pagoDiv, nombreLabel, montoLabel, fechaLabel, recurrenteLabel, frecuenciaLabel));
        editButton.addClassName("action-button");

        Button deleteButton = new Button("Eliminar", event -> eliminarPago(pagoId, pagoDiv));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClassName("pago-eliminar");

        Div buttonContainer = new Div(editButton, deleteButton);
        buttonContainer.setClassName("pago-buttons");

        pagoDiv.add(nombreLabel, montoLabel, fechaLabel, recurrenteLabel, frecuenciaLabel, buttonContainer);

        return pagoDiv;
    }

    private void openAddPagoDialog() {
        Dialog addPagoDialog = new Dialog();
        addPagoDialog.setHeaderTitle("Nuevo Pago");

        TextField nameField = new TextField("Nombre del Pago");
        nameField.setPlaceholder("Ej: Alquiler");

        NumberField amountField = new NumberField("Monto");
        amountField.setPrefixComponent(new Span("€"));
        amountField.setPlaceholder("Ej: 500.00");

        DatePicker dateField = new DatePicker("Fecha");
        dateField.setPlaceholder("Selecciona una fecha");

        Checkbox recurrenteCheckbox = new Checkbox("Recurrente");

        ComboBox<String> frecuenciaComboBox = new ComboBox<>("Frecuencia");
        frecuenciaComboBox.setItems("diaria", "semanal", "mensual", "anual");
        frecuenciaComboBox.setEnabled(false);

        recurrenteCheckbox.addValueChangeListener(event -> frecuenciaComboBox.setEnabled(event.getValue()));

        Button saveButton = new Button("Guardar", event -> {
            String nombre = nameField.getValue();
            Double monto = amountField.getValue();
            LocalDate fecha = dateField.getValue();
            Boolean recurrente = recurrenteCheckbox.getValue();
            String frecuencia = recurrente ? frecuenciaComboBox.getValue().toUpperCase() : null; // Convierte el valor recibido en mayusc para que lo entienda el backend

            if (nombre.isEmpty() || monto == null || fecha == null || (recurrente && frecuencia == null)) {
                Notification.show("Todos los campos son obligatorios.");
                return;
            }

            Map<String, Object> pagoData = new HashMap<>();
            pagoData.put("nombre", nameField.getValue());
            pagoData.put("monto", amountField.getValue());
            pagoData.put("fecha", dateField.getValue().toString());
            pagoData.put("recurrente", recurrenteCheckbox.getValue());

            if (recurrenteCheckbox.getValue()) {
                pagoData.put("frecuencia", frecuenciaComboBox.getValue().toUpperCase());
            }


            Optional<Map<String, Object>> result = pagoService.createPago(usuarioId, pagoData);
            if (result.isPresent()) {
                Map<String, Object> newPago = result.get();
                Integer newPagoId = (Integer) newPago.get("id");
                Div pagoDiv = createPagoDiv(newPagoId, nombre, monto, fecha.toString(), recurrente, frecuencia);
                pagosContainer.add(pagoDiv);
                addPagoDialog.close();
            } else {
                Notification.show("Error al agregar el pago. Inténtalo nuevamente.");
            }
        });

        Button cancelButton = new Button("Cancelar", event -> addPagoDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        addPagoDialog.add(nameField, amountField, dateField, recurrenteCheckbox, frecuenciaComboBox, buttonsLayout);
        addPagoDialog.open();
    }

    private void openEditPagoDialog(Integer pagoId, Div pagoDiv, NativeLabel nombreLabel, NativeLabel montoLabel, NativeLabel fechaLabel, NativeLabel recurrenteLabel, NativeLabel frecuenciaLabel) {
        Dialog editDialog = new Dialog();
        editDialog.setHeaderTitle("Editar Pago");

        TextField nameField = new TextField("Nombre del Pago");
        nameField.setValue(nombreLabel.getText().replace("Nombre: ", ""));

        NumberField amountField = new NumberField("Monto");
        amountField.setValue(Double.parseDouble(montoLabel.getText().replace("Monto: ", "").replace(" €", "")));

        DatePicker dateField = new DatePicker("Fecha");
        dateField.setValue(LocalDate.parse(fechaLabel.getText().replace("Fecha: ", "")));

        Checkbox recurrenteCheckbox = new Checkbox("Recurrente", recurrenteLabel.getText().contains("Sí"));

        ComboBox<String> frecuenciaComboBox = new ComboBox<>("Frecuencia");
        frecuenciaComboBox.setItems("diaria", "semanal", "mensual", "anual");
        frecuenciaComboBox.setValue(frecuenciaLabel.getText().replace("Frecuencia: ", "").equals("N/A") ? null : frecuenciaLabel.getText().replace("Frecuencia: ", ""));
        frecuenciaComboBox.setEnabled(recurrenteCheckbox.getValue());

        recurrenteCheckbox.addValueChangeListener(event -> frecuenciaComboBox.setEnabled(event.getValue()));

        Button saveButton = new Button("Guardar", event -> {
            String nuevoNombre = nameField.getValue();
            Double nuevoMonto = amountField.getValue();
            LocalDate nuevaFecha = dateField.getValue();
            Boolean recurrente = recurrenteCheckbox.getValue();
            String nuevaFrecuencia = recurrente ? frecuenciaComboBox.getValue() : null;

            if (nuevoNombre.isEmpty() || nuevoMonto == null || nuevaFecha == null || (recurrente && nuevaFrecuencia == null)) {
                Notification.show("Todos los campos son obligatorios.");
                return;
            }

            boolean success = pagoService.updatePago(pagoId, nuevoNombre, nuevoMonto, nuevaFecha, recurrente, nuevaFrecuencia);
            if (success) {
                Notification.show("Pago actualizado con éxito.");
                editDialog.close();

                nombreLabel.setText("Nombre: " + nuevoNombre);
                montoLabel.setText("Monto: " + nuevoMonto + " €");
                fechaLabel.setText("Fecha: " + nuevaFecha.toString());
                recurrenteLabel.setText("Recurrente: " + (recurrente ? "Sí" : "No"));
                frecuenciaLabel.setText("Frecuencia: " + (recurrente ? nuevaFrecuencia : "N/A"));
            } else {
                Notification.show("Error al actualizar el pago.");
            }
        });

        Button cancelButton = new Button("Cancelar", event -> editDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        editDialog.add(nameField, amountField, dateField, recurrenteCheckbox, frecuenciaComboBox, buttonsLayout);
        editDialog.open();
    }

    private void eliminarPago(Integer pagoId, Div pagoDiv) {
        boolean success = pagoService.deletePago(pagoId);
        if (success) {
            Notification.show("Pago eliminado con éxito.");
            pagosContainer.remove(pagoDiv);
        } else {
            Notification.show("Error al eliminar el pago.");
        }
    }
}

