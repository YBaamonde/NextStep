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
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.dataprovider.InMemoryEntryProvider;

import java.io.ByteArrayInputStream;
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

        // Espaciador para pantallas móviles
        Div spacer = new Div();
        spacer.setHeight("150px");
        add(spacer);
    }

    private void agregarElementosVista(Map<String, Object> datosInicio) {
        List<Map<String, Object>> pagos = (List<Map<String, Object>>) datosInicio.getOrDefault("pagosProximos", List.of());
        Map<String, Double> gastosPorCategoria = (Map<String, Double>) datosInicio.getOrDefault("gastosPorCategoria", Map.of());
        Map<String, Double> evolucionTrimestral = (Map<String, Double>) datosInicio.getOrDefault("evolucionTrimestral", Map.of());

        // Paneles
        HorizontalLayout panelesContainer = new HorizontalLayout();
        panelesContainer.setClassName("paneles-container");
        panelesContainer.getStyle().set("height", "100%").set("align-items", "stretch");

        // Panel de pagos
        Div panelPagos = crearPanelPagos(pagos);

        // Panel de gastos
        Div panelCategorias = crearPanelConGrafico("Gastos por Categoría", crearGraficoCircular(gastosPorCategoria));
        // El botón para añadir gastos lleva a la vista de gastos por si no existen categorías creadas
        Button addGastoButton = new Button("Añadir Gasto", e -> UI.getCurrent().navigate(GastosView.class));
        addGastoButton.setClassName("boton-panel");
        panelCategorias.add(addGastoButton);

        // Panel de trimestres
        Div panelTrimestres = crearPanelConGrafico("Evolución por Trimestre", crearGraficoBarras(evolucionTrimestral));
        Button createInformeButton = new Button("Generar Informe");
        createInformeButton.setClassName("boton-panel");
        createInformeButton.addClickListener(e -> generarInforme());
        panelTrimestres.add(createInformeButton);

        panelesContainer.add(panelPagos, panelCategorias, panelTrimestres);
        add(panelesContainer);
    }

    private void generarInforme() {
        inicioService.generarInformePdf(usuarioId).ifPresentOrElse(
                pdfBytes -> {
                    // Crear un recurso para el PDF
                    StreamResource pdfResource = new StreamResource("informe.pdf", () -> new ByteArrayInputStream(pdfBytes));
                    pdfResource.setContentType("application/pdf");
                    pdfResource.setCacheTime(0);

                    // Registrar el recurso en la sesión actual
                    StreamRegistration registration = UI.getCurrent().getSession().getResourceRegistry().registerResource(pdfResource);

                    // Obtener la URL del recurso registrado
                    String pdfUrl = registration.getResourceUri().toString();

                    // Abrir el PDF en una nueva pestaña
                    UI.getCurrent().getPage().open(pdfUrl, "_blank");

                    Notification.show("Informe generado correctamente y abierto en una nueva pestaña.");
                },
                () -> Notification.show("Error al generar el informe. Verifica tus permisos o inicia sesión nuevamente.")
        );
    }




    private Div crearPanelPagos(List<Map<String, Object>> pagos) {
        Div panel = new Div();
        panel.setClassName("panel");
        panel.getStyle().set("display", "flex").set("flex-direction", "column").set("justify-content", "space-between").set("height", "100%");

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
        List<String> palette = Arrays.asList("#FF5722", "#0074DB", "#FFC107", "#4CAF50", "#E91E63");

        // Mapa para asociar colores únicos a cada pago
        Map<Integer, String> pagoColorMap = new HashMap<>();
        AtomicInteger colorIndex = new AtomicInteger(0);

        // Agregar los pagos al calendario
        if (!pagos.isEmpty()) {
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

        panel.add(calendar, addPagoButton);

        return panel;
    }

    private Div crearPanelConGrafico(String titulo, ApexCharts grafico) {
        Div panel = new Div();
        panel.setClassName("panel");
        panel.getStyle().set("display", "flex").set("flex-direction", "column").set("justify-content", "space-between").set("height", "100%");

        H2 tituloPanel = new H2(titulo);
        tituloPanel.addClassName("panel-title");

        // Espaciador para ajustar los gráficos
        grafico.getStyle().set("margin-bottom", "auto");

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

    private ApexCharts crearGraficoBarras(Map<String, Double> datos) {
        String[] trimestres = datos.keySet().toArray(new String[0]);
        Double[] valores = datos.values().toArray(new Double[0]);

        return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.BAR).build())
                .withLabels(trimestres)
                .withSeries(new Series<>("Gastos", valores))
                .withColors("#FF5722")
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
