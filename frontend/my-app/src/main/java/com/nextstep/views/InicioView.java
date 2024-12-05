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
import java.util.concurrent.atomic.AtomicInteger;

@Route("")
@RouteAlias("inicio")
@PageTitle("Inicio | NextStep")
@CssImport("./themes/nextstepfrontend/inicio-view.css")
public class InicioView extends VerticalLayout {

    private final InicioService inicioService;
    private final Integer usuarioId;
    private FullCalendar calendar;
    private InMemoryEntryProvider<Entry> entryProvider;

    @Autowired
    public InicioView(InicioService inicioService) {
        this.inicioService = inicioService;

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
        usuarioId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if (usuarioId == null) {
            Notification.show("Usuario no autenticado. Por favor, inicia sesión.");
            return;
        }

        // Llamar al servicio para obtener los datos
        Optional<Map<String, Object>> inicioData = inicioService.getInicioData(usuarioId);

        // Construir la vista según los datos
        if (inicioData.isPresent()) {
            Map<String, Object> data = inicioData.get();
            agregarElementosVista(data);
        } else {
            Notification.show("Error al cargar los datos de inicio.");
        }
    }

    private void agregarElementosVista(Map<String, Object> datosInicio) {
        List<Map<String, Object>> pagos = (List<Map<String, Object>>) datosInicio.getOrDefault("pagosProximos", List.of());
        Map<String, Double> gastosPorCategoria = (Map<String, Double>) datosInicio.getOrDefault("gastosPorCategoria", Map.of());
        Map<String, Double> evolucionTrimestral = (Map<String, Double>) datosInicio.getOrDefault("evolucionTrimestral", Map.of());

        // Paneles
        HorizontalLayout panelesContainer = new HorizontalLayout();
        panelesContainer.setClassName("paneles-container");

        // Panel de pagos con botón para agregar pago
        Div panelPagos = crearPanelPagos(pagos);

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

    private Div crearPanelPagos(List<Map<String, Object>> pagos) {
        Div panel = new Div();
        panel.setClassName("panel");

        // Título del panel
        H2 titulo = new H2("Pagos Próximos");
        titulo.addClassName("panel-title");
        panel.add(titulo);

        // Crear el calendario
        calendar = FullCalendarBuilder.create().build();
        calendar.setLocale(new Locale("es", "ES"));
        calendar.setWeekNumbersVisible(false);

        // Obtener el EntryProvider como InMemory
        entryProvider = calendar.getEntryProvider().asInMemory();

        // Paleta de colores
        List<String> palette = Arrays.asList(
                "#FF5722", // Naranja
                "#0074DB", // Azul
                "#FFC107", // Amarillo
                "#4CAF50", // Verde
                "#E91E63"  // Rosa
        );

        // Mapa para asociar colores únicos a cada pago
        Map<Integer, String> pagoColorMap = new HashMap<>();
        AtomicInteger colorIndex = new AtomicInteger(0); // Índice para recorrer la paleta

        // Agregar los pagos al calendario
        if (!pagos.isEmpty()) {
            pagos.forEach(pago -> {
                try {
                    Integer pagoId = (Integer) pago.get("id"); // ID del pago original
                    String nombre = (String) pago.get("nombre");
                    String fechaStr = (String) pago.get("fecha");
                    LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    // Asignar un color único al pago
                    String color = pagoColorMap.computeIfAbsent(pagoId, id -> {
                        return palette.get(colorIndex.getAndIncrement() % palette.size());
                    });

                    // Crear la entrada en el calendario
                    Entry entry = new Entry();
                    entry.setTitle(nombre);
                    entry.setStart(fecha.atStartOfDay());
                    entry.setEnd(fecha.plusDays(1).atStartOfDay());
                    entry.setAllDay(true);
                    entry.setColor(color); // Aplicar color único

                    entryProvider.addEntry(entry);
                } catch (Exception e) {
                    System.err.println("Error al procesar el pago: " + pago + ". Detalle: " + e.getMessage());
                }
            });
        }

        // Botón para añadir pago
        Button addPagoButton = new Button("Añadir Pago", e -> {
            new PagosView().openAddPagoDialog();
            actualizarCalendario();
        });
        addPagoButton.setClassName("boton-panel");

        // Agregar elementos al panel
        panel.add(calendar, addPagoButton);

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

    void actualizarCalendario() {
        inicioService.getInicioData(usuarioId).ifPresent(datos -> {
            List<Map<String, Object>> pagos = (List<Map<String, Object>>) datos.getOrDefault("pagosProximos", List.of());
            entryProvider.removeAllEntries();
            agregarPagosACalendario(pagos);
        });
    }

    private void agregarPagosACalendario(List<Map<String, Object>> pagos) {
        List<String> palette = Arrays.asList(
                "#FF5722", "#0074DB", "#FFC107", "#4CAF50", "#E91E63"
        );
        AtomicInteger colorIndex = new AtomicInteger(0);

        Map<Integer, String> pagoColorMap = new HashMap<>();

        pagos.forEach(pago -> {
            try {
                Integer pagoId = (Integer) pago.get("id");
                String nombre = (String) pago.get("nombre");
                String fechaStr = (String) pago.get("fecha");
                LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                String color = pagoColorMap.computeIfAbsent(pagoId, id -> palette.get(colorIndex.getAndIncrement() % palette.size()));

                Entry entry = new Entry();
                entry.setTitle(nombre);
                entry.setStart(fecha.atStartOfDay());
                entry.setEnd(fecha.plusDays(1).atStartOfDay());
                entry.setAllDay(true);
                entry.setColor(color);

                entryProvider.addEntry(entry);
            } catch (Exception e) {
                System.err.println("Error al actualizar el calendario: " + pago + ". Detalle: " + e.getMessage());
            }
        });
    }
}
