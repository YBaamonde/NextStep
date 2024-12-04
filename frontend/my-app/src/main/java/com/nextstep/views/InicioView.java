package com.nextstep.views;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.nextstep.services.InicioService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Route("")
@RouteAlias("inicio")
@PageTitle("Inicio | NextStep")
@CssImport("./themes/nextstepfrontend/inicio-view.css")
public class InicioView extends VerticalLayout {

    private final InicioService inicioService;

    @Autowired
    public InicioView(InicioService inicioService) {
        this.inicioService = inicioService;

        addClassName("inicio-view");

        // Obtén el usuario actual desde la sesión
        Integer usuarioId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if (usuarioId == null) {
            Notification.show("Usuario no autenticado. Por favor, inicia sesión.");
            return;
        }

        // Llama al servicio para obtener los datos de inicio
        Optional<Map<String, Object>> inicioData = inicioService.getInicioData(usuarioId);

        if (inicioData.isPresent()) {
            Map<String, Object> data = inicioData.get();
            agregarElementosVista(data);
        } else {
            Notification.show("Error al cargar los datos de inicio.");
        }
    }

    private void agregarElementosVista(Map<String, Object> datosInicio) {
        // Obtén los datos de las distintas secciones
        List<Map<String, Object>> pagos = (List<Map<String, Object>>) datosInicio.getOrDefault("pagosProximos", List.of());
        Map<String, Double> gastosPorCategoria = (Map<String, Double>) datosInicio.getOrDefault("gastosPorCategoria", Map.of());
        Map<String, Double> gastosPorTrimestre = (Map<String, Double>) datosInicio.getOrDefault("evolucionTrimestral", Map.of());

        // Crear contenedor para los paneles
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("inicio-view");

        // Agregar panel de pagos próximos
        Div panelPagos = crearPanelPagosProximos(pagos);
        layout.add(panelPagos);

        // Agregar gráfico de categorías
        ApexCharts graficoCategorias = crearGraficoCategorias(gastosPorCategoria);
        Div panelCategorias = crearPanelConGrafico("Gastos por Categoría", graficoCategorias);
        layout.add(panelCategorias);

        // Agregar gráfico de trimestres
        ApexCharts graficoTrimestres = crearGraficoTrimestres(gastosPorTrimestre);
        Div panelTrimestres = crearPanelConGrafico("Gastos por Trimestre", graficoTrimestres);
        layout.add(panelTrimestres);

        add(layout);
    }

    // Crear gráfico de categorías
    private ApexCharts crearGraficoCategorias(Map<String, Double> datos) {
        String[] categorias = datos.keySet().toArray(new String[0]);
        Double[] valores = datos.values().toArray(new Double[0]);

        return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.PIE).build())
                .withLabels(categorias)
                .withSeries(valores)
                .build();
    }

    // Crear gráfico de trimestres
    private ApexCharts crearGraficoTrimestres(Map<String, Double> datos) {
        String[] trimestres = datos.keySet().toArray(new String[0]);
        Double[] valores = datos.values().toArray(new Double[0]);

        return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.BAR).build())
                .withXaxis(XAxisBuilder.get().withCategories(trimestres).build())
                .withSeries(new Series<>("Evolución", valores))
                .build();
    }

    // Crear panel contenedor de gráficos
    private Div crearPanelConGrafico(String titulo, ApexCharts grafico) {
        Div panel = new Div();
        panel.addClassName("panel-grafico");

        H2 tituloPanel = new H2(titulo);
        tituloPanel.addClassName("panel-title");

        panel.add(tituloPanel, grafico);
        return panel;
    }

    // Crear panel de pagos
    private Div crearPanelPagosProximos(List<Map<String, Object>> pagos) {
        Div panel = new Div();
        panel.addClassName("panel");

        H2 titulo = new H2("Pagos Próximos");
        titulo.addClassName("panel-title");
        panel.add(titulo);

        if (pagos.isEmpty()) {
            panel.add(new Text("No hay pagos próximos."));
        } else {
            pagos.forEach(pago -> {
                String detalle = pago.get("nombre") + " - " + pago.get("fecha");
                Div pagoDiv = new Div(new Text(detalle));
                pagoDiv.addClassName("pago-item");
                panel.add(pagoDiv);
            });
        }

        return panel;
    }

}
