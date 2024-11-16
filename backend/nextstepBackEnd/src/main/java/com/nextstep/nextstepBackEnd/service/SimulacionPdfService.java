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
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Agregar el logotipo
            Image logo = Image.getInstance("src/main/resources/media/logo03.png");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
            document.add(new Paragraph(" "));

            // Título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Simulación Financiera", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Información básica
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
            document.add(new Paragraph("Ingresos: " + simulacionDTO.getIngresos() + " €", infoFont));
            document.add(new Paragraph("Balance Proyectado: " + simulacionDTO.getBalanceProyectado() + " €", infoFont));
            document.add(new Paragraph("Meses de Simulación: " + simulacionDTO.getMesesSimulacion(), infoFont));
            document.add(new Paragraph("Meta de Ahorro: " + simulacionDTO.getMetaAhorro() + " €", infoFont));
            document.add(new Paragraph(" "));

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

            for (Map.Entry<String, Map<String, Double>> categoria : simulacionDTO.getGastosClasificados().entrySet()) {
                for (Map.Entry<String, Double> gasto : categoria.getValue().entrySet()) {
                    gastosTable.addCell(new Phrase(gasto.getKey(), infoFont));
                    gastosTable.addCell(new Phrase(String.format("%.2f", gasto.getValue()), infoFont));
                }
            }
            document.add(gastosTable);

            // Recomendaciones
            document.add(new Paragraph("Recomendaciones:", infoFont));
            for (String recomendacion : simulacionDTO.getRecomendaciones()) {
                document.add(new Paragraph("- " + recomendacion, infoFont));
            }

            document.close();
            return byteArrayOutputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
