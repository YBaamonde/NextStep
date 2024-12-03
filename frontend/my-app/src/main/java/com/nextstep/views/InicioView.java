package com.nextstep.views;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.helper.Series;
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
        List<Map<String, Object>> pagos = (List<Map<String, Object>>) datosInicio.getOrDefault("pagos", List.of());
        Map<String, Double> gastosPorCategoria = (Map<String, Double>) datosInicio.getOrDefault("gastosPorCategoria", Map.of());
        Map<String, Double> gastosPorTrimestre = (Map<String, Double>) datosInicio.getOrDefault("gastosPorTrimestre", Map.of());

        // Panel con pagos próximos
        Div panelPagos = crearPanelPagosProximos(pagos);
        add(panelPagos);

        // Gráfico de categorías
        ApexCharts graficoCategorias = crearGraficoCategorias(gastosPorCategoria);
        add(crearPanelConGrafico("Gastos por Categoría", graficoCategorias));

        // Gráfico de trimestres
        ApexCharts graficoTrimestres = crearGraficoTrimestres(gastosPorTrimestre);
        add(crearPanelConGrafico("Gastos por Trimestre", graficoTrimestres));
    }

    private Div crearPanelPagosProximos(List<Map<String, Object>> pagos) {
        Div panel = new Div();
        panel.addClassName("panel");

        H2 titulo = new H2("Pagos Próximos");
        panel.add(titulo);

        if (pagos.isEmpty()) {
            panel.add(new Text("No hay pagos próximos."));
        } else {
            pagos.forEach(pago -> {
                Div pagoDiv = new Div(new Text(pago.get("nombre") + " - " + pago.get("fecha")));
                pagoDiv.addClassName("pago-item");
                panel.add(pagoDiv);
            });
        }

        return panel;
    }

    private Div crearPanelConGrafico(String titulo, ApexCharts grafico) {
        Div panel = new Div();
        panel.addClassName("panel");

        H2 tituloPanel = new H2(titulo);
        panel.add(tituloPanel, grafico);

        return panel;
    }

    private ApexCharts crearGraficoCategorias(Map<String, Double> gastosPorCategoria) {
        String[] categorias = gastosPorCategoria.keySet().toArray(new String[0]);
        Double[] valores = gastosPorCategoria.values().toArray(new Double[0]);

        return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.PIE).build())
                .withLabels(categorias)
                .withSeries(valores)
                .build();
    }

    private ApexCharts crearGraficoTrimestres(Map<String, Double> gastosPorTrimestre) {
        String[] trimestres = gastosPorTrimestre.keySet().toArray(new String[0]);
        Double[] valores = gastosPorTrimestre.values().toArray(new Double[0]);

        return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.BAR).build())
                .withXaxis(XAxisBuilder.get().withCategories(trimestres).build())
                .withSeries(new Series<>("Gastos", valores))
                .build();
    }
}
