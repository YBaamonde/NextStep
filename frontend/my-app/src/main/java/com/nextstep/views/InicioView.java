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

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        Div panelPagos = crearPanelPagos(pagos);
        Div panelCategorias = crearPanelConGrafico("Gastos por Categoría", crearGraficoCircular(gastosPorCategoria));
        Div panelTrimestres = crearPanelConGrafico("Evolución por Trimestre", crearGraficoBarra(evolucionTrimestral));

        panelesContainer.add(panelPagos, panelCategorias, panelTrimestres);
        add(panelesContainer);
    }

    private Div crearPanelPagos(List<Map<String, Object>> pagos) {
        Div panel = new Div();
        panel.setClassName("panel");

        H2 titulo = new H2("Pagos Próximos");
        titulo.addClassName("panel-title");
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
                .withChart(ChartBuilder.get().withType(Type.DONUT).build()) // Se construye un objeto Chart
                .withLabels(categorias)
                .withSeries(valores)
                .build();
    }

    private ApexCharts crearGraficoBarra(Map<String, Double> datos) {
        // Convertir las claves del mapa en nombres de trimestres
        String[] trimestres = datos.keySet().toArray(new String[0]);
        Double[] valores = datos.values().toArray(new Double[0]);

        return ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.BAR).build()) // Construir el objeto Chart
                .withLabels(trimestres)
                .withSeries(new Series<>("Gastos", valores))
                .build();
    }


}
