package com.nextstep.nextstepBackEnd.service.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PdfService {
    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

    public Document iniciarDocumento(ByteArrayOutputStream byteArrayOutputStream) throws DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        // Agregar logotipo
        try {
            Image logo = Image.getInstance("src/main/resources/media/logo03.png");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
            document.add(new Paragraph(" "));
        } catch (Exception e) {
            //System.out.println("Error al agregar el logotipo al PDF: " + e.getMessage());
            logger.error("Error al agregar el logotipo al PDF", e);
        }

        return document;
    }

    public void cerrarDocumento(Document document) {
        if (document != null && document.isOpen()) {
            document.close();
        }
    }
}
