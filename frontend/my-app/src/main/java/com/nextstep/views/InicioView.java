package com.nextstep.views;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.helper.Series;
import com.nextstep.services.AuthService;
import com.nextstep.services.InAppNotifService;
import com.nextstep.services.InicioService;
import com.nextstep.views.components.MainNavbar;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.dataprovider.InMemoryEntryProvider;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Route("")
@RouteAlias("inicio")
@PageTitle("Inicio | NextStep")
@CssImport("./themes/nextstepfrontend/inicio-view.css")
public class InicioView extends VerticalLayout {

    @Autowired
    public InicioView(InicioService inicioService) {
        setClassName("inicio-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Navbar
        AuthService authService = new AuthService();
        InAppNotifService inAppNotifService = new InAppNotifService();
        MainNavbar navbar = new MainNavbar(authService, inAppNotifService);
        add(navbar);

        // Obtener datos del usuario
        Integer usuarioId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if (usuarioId == null) {
            Notification.show("Usuario no autenticado. Por favor, inicia sesión.");
            return;
        }

        // Llamar al servicio para obtener los datos
        Optional<Map<String, Object>> inicioData = inicioService.getInicioData(usuarioId);

        // Construir la vista según los datos
        if (inicioData.isPresent()) {
            Map<String, Object> data = inicioData.get();
            agregarElementosVista(data, usuarioId);
        } else {
            Notification.show("Error al cargar los datos de inicio.");
        }
    }

    private void agregarElementosVista(Map<String, Object> datosInicio, Integer usuarioId) {
        List<Map<String, Object>> pagos = (List<Map<String, Object>>) datosInicio.getOrDefault("pagosProximos", List.of());
        Map<String, Double> gastosPorCategoria = (Map<String, Double>) datosInicio.getOrDefault("gastosPorCategoria", Map.of());
        Map<String, Double> evolucionTrimestral = (Map<String, Double>) datosInicio.getOrDefault("evolucionTrimestral", Map.of());

        // Paneles
        HorizontalLayout panelesContainer = new HorizontalLayout();
        panelesContainer.setClassName("paneles-container");

        // Panel de pagos con botón para agregar pago
        Div panelPagos = crearPanelPagos(pagos, usuarioId);

        // Panel de categorías con botones para agregar categoría y gasto
        Div panelCategorias = crearPanelConGrafico("Gastos por Categoría", crearGraficoCircular(gastosPorCategoria));
        Button addGastoButton = new Button("Añadir Gasto", e -> new GastosView().openAddGastoDialog(1, new VerticalLayout()));
        addGastoButton.setClassName("boton-panel");
        panelCategorias.add(addGastoButton);

        // Panel de trimestres con botón para crear informe
        Div panelTrimestres = crearPanelConGrafico("Evolución por Trimestre", crearGraficoBarra(evolucionTrimestral));
        Button createInformeButton = new Button("Crear Informe");
        createInformeButton.setClassName("boton-panel");
        panelTrimestres.add(createInformeButton);

        panelesContainer.add(panelPagos, panelCategorias, panelTrimestres);
        add(panelesContainer);
    }

    private Div crearPanelPagos(List<Map<String, Object>> pagos, Integer usuarioId) {
        Div panel = new Div();
        panel.setClassName("panel");

        // Definir una paleta de colores relacionados con la estética de la aplicación
        List<String> palette = Arrays.asList(
                "#FF5722", // Naranja principal de la app
                "#0074DB", // Azul
                "#FFC107", // Amarillo
                "#4CAF50", // Verde
                "#E91E63"  // Rosa
        );

        // Configurar idioma español
        Locale spanish = new Locale("es", "ES");

        // Título del panel
        H2 titulo = new H2("Pagos Próximos");
        titulo.addClassName("panel-title");
        panel.add(titulo);

        // Crear el calendario
        FullCalendar calendar = FullCalendarBuilder.create().build();

        // Configurar idioma del calendario
        calendar.setLocale(spanish);
        // Eliminar número de semana
        calendar.setWeekNumbersVisible(false);

        // Obtener el EntryProvider como InMemory
        InMemoryEntryProvider<Entry> entryProvider = calendar.getEntryProvider().asInMemory();

        // Agregar las entradas de los pagos al EntryProvider
        if (!pagos.isEmpty()) {
            pagos.forEach(pago -> {
                try {
                    // Extraer datos del pago
                    //String nombre = (String) pago.get("nombre");
                    String fechaStr = (String) pago.get("fecha");

                    // Parsear la fecha
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate fecha = LocalDate.parse(fechaStr, formatter);

                    // Crear la entrada del calendario
                    Entry entry = new Entry();
                    entry.setTitle("");
                    entry.setStart(fecha.atStartOfDay()); // Convertir LocalDate a LocalDateTime
                    entry.setEnd(fecha.plusDays(1).atStartOfDay()); // Opcional: marcar el final del día
                    entry.setAllDay(true); // Evento de día completo
                    // Color distinto para cada pago
                    entry.setColor(palette.get(pagos.indexOf(pago) % palette.size()));

                    // Log para depuración
                    //System.out.println("Agregando entrada: " + entry.getTitle() + " - " + entry.getStart());

                    // Agregar la entrada al EntryProvider
                    entryProvider.addEntry(entry);
                } catch (Exception e) {
                    System.err.println("Error al procesar el pago: " + pago + ". Detalle: " + e.getMessage());
                }
            });
        } else {
            panel.add(new Text("No hay pagos próximos."));
        }

        // Botón para añadir pago
        Button addPagoButton = new Button("Añadir Pago", e -> new PagosView().openAddPagoDialog());
        addPagoButton.setClassName("boton-panel");


        // Agregar elementos al panel
        panel.add(calendar, addPagoButton);

        // Logs finales para depuración
        /*
        System.out.println("Calendario HTML generado: " + calendar.getElement().getOuterHTML());
        entryProvider.fetchAll().forEach(entry -> {
            System.out.println("Entrada visible: " + entry.getTitle() + " - " + entry.getStart());
        });
         */

        // Refrescar manualmente el EntryProvider
        entryProvider.refreshAll();

        return panel;
    }


    private Div crearPanelConGrafico(String titulo, ApexCharts grafico) {
        Div panel = new Div();
        panel.setClassName("panel");

        H2 tituloPanel = new H2(titulo);
        tituloPanel.addClassName("panel-title");
        panel.add(tituloPanel, grafico);

        return panel;
    }

    private ApexCharts crearGraficoCircular(Map<String, Double> datos) {
        String[] categorias = datos.keySet().toArray(new String[0]);
        Double[] valores = datos.values().toArray(new Double[0]);

        return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.DONUT).build())
                .withLabels(categorias)
                .withSeries(valores)
                .build();
    }

    private ApexCharts crearGraficoBarra(Map<String, Double> datos) {
        String[] trimestres = datos.keySet().toArray(new String[0]);
        Double[] valores = datos.values().toArray(new Double[0]);

        return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.BAR).build())
                .withLabels(trimestres)
                .withSeries(new Series<>("Gastos", valores))
                .build();
    }
}
