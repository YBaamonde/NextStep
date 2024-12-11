package com.nextstep.nextstepBackEnd.service.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class InformePdfService extends PdfService {

    public byte[] generarPdfSimulacion(SimulacionDTO simulacionDTO) {
        // Validar datos
        if (simulacionDTO == null
                || simulacionDTO.getIngresos() <= 0
                || simulacionDTO.getMesesSimulacion() <= 0
                || simulacionDTO.getGastosClasificados() == null
                || simulacionDTO.getGastosClasificados().isEmpty()) {
            throw new RuntimeException("Datos inválidos para generar el PDF de simulación");
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = iniciarDocumento(byteArrayOutputStream);

            agregarTitulo(document, "Simulación Financiera");
            agregarInformacionBasicaSimulacion(document, simulacionDTO);
            agregarTablaGastosClasificados(document, simulacionDTO);
            agregarProporcionesGastos(document, simulacionDTO);
            agregarRecomendacionesPdfSimu(document, simulacionDTO);

            cerrarDocumento(document);
            return byteArrayOutputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error al generar el PDF de simulación", e);
        }
    }

    public byte[] generarPdfEvolucionTrimestral(Map<String, Double> evolucionTrimestral) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = iniciarDocumento(byteArrayOutputStream);

            agregarTitulo(document, "Evolución Trimestral de Gastos");
            agregarTablaEvolucionTrimestral(document, evolucionTrimestral);
            agregarResumenTotales(document, evolucionTrimestral);
            agregarDistribucionGastos(document, evolucionTrimestral);
            agregarRecomendacionesPdfInicio(document, evolucionTrimestral);

            cerrarDocumento(document);
            return byteArrayOutputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar el PDF de evolución trimestral");
        }
    }



    private void agregarTitulo(Document document, String tituloTexto) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph(tituloTexto, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
    }

    private void agregarInformacionBasicaSimulacion(Document document, SimulacionDTO simulacionDTO) throws DocumentException {
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
        document.add(new Paragraph("Ingresos: " + simulacionDTO.getIngresos() + " €", infoFont));
        document.add(new Paragraph("Balance Proyectado: " + simulacionDTO.getBalanceProyectado() + " €", infoFont));
        document.add(new Paragraph("Meses de Simulación: " + simulacionDTO.getMesesSimulacion(), infoFont));
        document.add(new Paragraph("Meta de Ahorro: " + simulacionDTO.getMetaAhorro() + " €", infoFont));
        document.add(new Paragraph(" "));
    }

    private void agregarTablaGastosClasificados(Document document, SimulacionDTO simulacionDTO) throws DocumentException {
        PdfPTable gastosTable = new PdfPTable(2);
        gastosTable.setWidthPercentage(100);
        gastosTable.setSpacingBefore(10f);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        PdfPCell categoriaHeader = new PdfPCell(new Phrase("Categoría", headerFont));
        categoriaHeader.setBackgroundColor(BaseColor.GRAY);
        PdfPCell montoHeader = new PdfPCell(new Phrase("Monto (€)", headerFont));
        montoHeader.setBackgroundColor(BaseColor.GRAY);
        gastosTable.addCell(categoriaHeader);
        gastosTable.addCell(montoHeader);

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
        for (Map.Entry<String, Map<String, Double>> categoria : simulacionDTO.getGastosClasificados().entrySet()) {
            for (Map.Entry<String, Double> gasto : categoria.getValue().entrySet()) {
                gastosTable.addCell(new Phrase(gasto.getKey(), cellFont));
                gastosTable.addCell(new Phrase(String.format("%.2f", gasto.getValue()), cellFont));
            }
        }
        document.add(gastosTable);
    }

    private void agregarProporcionesGastos(Document document, SimulacionDTO simulacionDTO) throws DocumentException {
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
        document.add(new Paragraph("Distribución de Gastos:", infoFont));
        document.add(new Paragraph("Gastos Esenciales: " + simulacionDTO.getProporciones().getOrDefault("esenciales", 0.0) + " %", infoFont));
        document.add(new Paragraph("Gastos Opcionales: " + simulacionDTO.getProporciones().getOrDefault("opcionales", 0.0) + " %", infoFont));
        document.add(new Paragraph(" "));
    }

    private void agregarRecomendacionesPdfSimu(Document document, SimulacionDTO simulacionDTO) throws DocumentException {
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
        document.add(new Paragraph("Recomendaciones:", infoFont));
        for (String recomendacion : simulacionDTO.getRecomendaciones()) {
            document.add(new Paragraph("- " + recomendacion, infoFont));
        }
    }

    private void agregarTablaEvolucionTrimestral(Document document, Map<String, Double> evolucionTrimestral) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Encabezados de la tabla
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        PdfPCell trimestreHeader = new PdfPCell(new Phrase("Trimestre", headerFont));
        trimestreHeader.setBackgroundColor(BaseColor.GRAY);
        PdfPCell montoHeader = new PdfPCell(new Phrase("Monto (€)", headerFont));
        montoHeader.setBackgroundColor(BaseColor.GRAY);
        table.addCell(trimestreHeader);
        table.addCell(montoHeader);

        // Agregar datos
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
        for (Map.Entry<String, Double> entry : evolucionTrimestral.entrySet()) {
            table.addCell(new PdfPCell(new Phrase(entry.getKey(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", entry.getValue()), cellFont)));
        }

        document.add(table);
    }


    private void agregarResumenTotales(Document document, Map<String, Double> evolucionTrimestral) throws DocumentException {
        double totalGastos = evolucionTrimestral.values().stream().mapToDouble(Double::doubleValue).sum();
        double promedio = totalGastos / evolucionTrimestral.size();
        double maximo = evolucionTrimestral.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        double minimo = evolucionTrimestral.values().stream().mapToDouble(Double::doubleValue).min().orElse(0.0);

        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
        document.add(new Paragraph("Resumen de Totales:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        document.add(new Paragraph("Total de Gastos: " + String.format("%.2f", totalGastos) + " €", infoFont));
        document.add(new Paragraph("Promedio Trimestral: " + String.format("%.2f", promedio) + " €", infoFont));
        document.add(new Paragraph("Máximo Trimestral: " + String.format("%.2f", maximo) + " €", infoFont));
        document.add(new Paragraph("Mínimo Trimestral: " + String.format("%.2f", minimo) + " €", infoFont));
        document.add(new Paragraph(" "));
    }

    private void agregarDistribucionGastos(Document document, Map<String, Double> evolucionTrimestral) throws DocumentException {
        double totalGastos = evolucionTrimestral.values().stream().mapToDouble(Double::doubleValue).sum();

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        PdfPCell trimestreHeader = new PdfPCell(new Phrase("Trimestre", headerFont));
        trimestreHeader.setBackgroundColor(BaseColor.GRAY);
        PdfPCell porcentajeHeader = new PdfPCell(new Phrase("Porcentaje (%)", headerFont));
        porcentajeHeader.setBackgroundColor(BaseColor.GRAY);
        table.addCell(trimestreHeader);
        table.addCell(porcentajeHeader);

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
        for (Map.Entry<String, Double> entry : evolucionTrimestral.entrySet()) {
            double porcentaje = (entry.getValue() / totalGastos) * 100;
            table.addCell(new PdfPCell(new Phrase(entry.getKey(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", porcentaje) + " %", cellFont)));
        }

        document.add(new Paragraph("Distribución de Gastos:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        document.add(table);
        document.add(new Paragraph(" "));
    }

    private void agregarRecomendacionesPdfInicio(Document document, Map<String, Double> evolucionTrimestral) throws DocumentException {
        double maximo = evolucionTrimestral.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        String trimestreMayorGasto = evolucionTrimestral.entrySet().stream()
                .filter(entry -> entry.getValue() == maximo)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("N/A");

        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
        document.add(new Paragraph("Recomendaciones Financieras:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK)));
        document.add(new Paragraph("- Reduzca gastos en el trimestre con mayor gasto: " + trimestreMayorGasto + ".", infoFont));
        document.add(new Paragraph("- Considere equilibrar los gastos trimestrales para una mejor estabilidad financiera.", infoFont));
        document.add(new Paragraph(" "));
    }

}
