package com.nextstep.views;

// Vista de inicio

import com.nextstep.services.InicioService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "inicio")
@RouteAlias(value = "")
@PageTitle("Inicio | NextStep")
@CssImport("./themes/nextstepfrontend/inicio-view.css")
public class InicioView extends VerticalLayout {

    private final InicioService inicioService;
    private final Integer usuarioId;

    public InicioView() {
        this.inicioService = new InicioService();

        // Obtener el usuario ID desde la sesión
        this.usuarioId = (Integer) com.vaadin.flow.server.VaadinSession.getCurrent().getAttribute("userId");
        if (this.usuarioId == null) {
            add(new H1("Error: No se pudo obtener el ID de usuario. Por favor, inicia sesión."));
            return;
        }

        setPadding(true);
        setSpacing(true);

        // Título principal
        H1 title = new H1("Bienvenido a tu panel de inicio");
        title.addClassName("title");
        add(title);

        // Cargar datos desde el servicio
        inicioService.getInicioData(usuarioId).ifPresentOrElse(this::cargarDatos, () -> {
            add(new H2("Error al cargar los datos de inicio. Intenta recargar la página."));
        });
    }

    private void cargarDatos(Map<String, Object> inicioData) {
        // Crear contenedores para secciones
        Div contenedorPagosProximos = crearSeccionPagosProximos((List<Map<String, Object>>) inicioData.get("pagosProximos"));
        Div contenedorGastosPorCategoria = crearSeccionGastosPorCategoria((Map<String, Object>) inicioData.get("gastosPorCategoria"));
        Div contenedorEvolucionTrimestral = crearSeccionEvolucionTrimestral((Map<String, Object>) inicioData.get("evolucionTrimestral"));

        // Agregar secciones a la vista
        add(contenedorPagosProximos, contenedorGastosPorCategoria, contenedorEvolucionTrimestral);
    }

    private Div crearSeccionPagosProximos(List<Map<String, Object>> pagosProximos) {
        Div contenedor = new Div();
        contenedor.add(new H2("Pagos Próximos"));

        if (pagosProximos == null || pagosProximos.isEmpty()) {
            contenedor.add(new Span("No hay pagos próximos."));
        } else {
            pagosProximos.forEach(pago -> {
                String nombre = (String) pago.get("nombre");
                BigDecimal monto = new BigDecimal(pago.get("monto").toString());
                String fecha = (String) pago.get("fecha");

                Span pagoSpan = new Span(nombre + ": " + monto + "€ - " + fecha);
                contenedor.add(pagoSpan);
            });
        }

        return contenedor;
    }

    private Div crearSeccionGastosPorCategoria(Map<String, Object> gastosPorCategoria) {
        Map<String, BigDecimal> gastosProcesados = new HashMap<>();

        // Convertir valores a BigDecimal
        gastosPorCategoria.forEach((categoria, monto) -> {
            if (monto instanceof BigDecimal) {
                gastosProcesados.put(categoria, (BigDecimal) monto);
            } else if (monto instanceof Double) {
                gastosProcesados.put(categoria, BigDecimal.valueOf((Double) monto));
            } else {
                throw new IllegalArgumentException("El monto debe ser de tipo BigDecimal o Double.");
            }
        });

        // Ahora utiliza gastosProcesados como Map<String, BigDecimal>
        Div contenedor = new Div();
        gastosProcesados.forEach((categoria, monto) -> {
            contenedor.add(new Span("Categoría: " + categoria + " - Monto: " + monto));
        });

        return contenedor;
    }


    private Div crearSeccionEvolucionTrimestral(Map<String, Object> evolucionTrimestral) {
        Div contenedor = new Div();
        contenedor.addClassName("evolucion-trimestral");

        evolucionTrimestral.forEach((key, value) -> {
            try {
                Integer trimestre = Integer.valueOf(key); // Convertir clave a Integer
                BigDecimal monto = new BigDecimal(value.toString()); // Convertir valor a BigDecimal
                contenedor.add(new Span("Trimestre: Q" + trimestre + " - Monto: " + monto + "€"));
            } catch (NumberFormatException | ClassCastException e) {
                throw new IllegalArgumentException("Formato incorrecto para evolución trimestral: " + e.getMessage());
            }
        });

        return contenedor;
    }


}
