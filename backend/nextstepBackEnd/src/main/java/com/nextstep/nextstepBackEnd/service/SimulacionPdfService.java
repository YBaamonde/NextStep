package com.nextstep.nextstepBackEnd.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class SimulacionPdfService {

    public byte[] generarPdfSimulacion(SimulacionDTO simulacionDTO) {
        System.out.println("Generando PDF con datos: " + simulacionDTO); // Debug

        if (simulacionDTO == null
                || simulacionDTO.getIngresos() == 0
                || simulacionDTO.getMesesSimulacion() == 0
                || simulacionDTO.getGastosClasificados() == null
                || simulacionDTO.getGastosClasificados().isEmpty()) {
            System.out.println("Datos inválidos para generar el PDF: " + simulacionDTO); // Debug
            throw new IllegalArgumentException("Datos inválidos para generar el PDF");
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            System.out.println("Documento PDF abierto correctamente."); // Debug

            // Agregar el logotipo
            try {
                Image logo = Image.getInstance("src/main/resources/media/logo03.png");
                logo.scaleToFit(100, 100);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
                document.add(new Paragraph(" "));
                System.out.println("Logotipo agregado al PDF."); // Debug
            } catch (Exception e) {
                System.out.println("Error al agregar el logotipo al PDF: " + e.getMessage()); // Debug
            }

            // Título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Simulación Financiera", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));
            System.out.println("Título agregado al PDF."); // Debug

            // Información básica
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
            document.add(new Paragraph("Ingresos: " + simulacionDTO.getIngresos() + " €", infoFont));
            document.add(new Paragraph("Balance Proyectado: " + simulacionDTO.getBalanceProyectado() + " €", infoFont));
            document.add(new Paragraph("Meses de Simulación: " + simulacionDTO.getMesesSimulacion(), infoFont));
            document.add(new Paragraph("Meta de Ahorro: " + simulacionDTO.getMetaAhorro() + " €", infoFont));
            document.add(new Paragraph(" "));
            System.out.println("Información básica agregada al PDF."); // Debug

            // Tabla de gastos clasificados
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

            System.out.println("Comenzando a agregar los gastos clasificados al PDF."); // Debug

            for (Map.Entry<String, Map<String, Double>> categoria : simulacionDTO.getGastosClasificados().entrySet()) {
                for (Map.Entry<String, Double> gasto : categoria.getValue().entrySet()) {
                    gastosTable.addCell(new Phrase(gasto.getKey(), infoFont));
                    gastosTable.addCell(new Phrase(String.format("%.2f", gasto.getValue()), infoFont));
                }
            }
            document.add(gastosTable);
            System.out.println("Gastos clasificados agregados al PDF."); // Debug

            // Proporciones de gastos
            document.add(new Paragraph("Distribución de Gastos:", infoFont));
            document.add(new Paragraph("Gastos Esenciales: " + simulacionDTO.getProporciones().getOrDefault("esenciales", 0.0) + " %", infoFont));
            document.add(new Paragraph("Gastos Opcionales: " + simulacionDTO.getProporciones().getOrDefault("opcionales", 0.0) + " %", infoFont));
            document.add(new Paragraph(" "));
            System.out.println("Proporciones de gastos agregadas al PDF."); // Debug

            // Recomendaciones
            document.add(new Paragraph("Recomendaciones:", infoFont));
            for (String recomendacion : simulacionDTO.getRecomendaciones()) {
                document.add(new Paragraph("- " + recomendacion, infoFont));
            }
            System.out.println("Recomendaciones agregadas al PDF."); // Debug

            document.close();
            System.out.println("PDF generado correctamente."); // Debug
            return byteArrayOutputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            System.out.println("Error al generar el PDF: " + e.getMessage()); // Debug
            e.printStackTrace();
            return null;
        }
    }


}
