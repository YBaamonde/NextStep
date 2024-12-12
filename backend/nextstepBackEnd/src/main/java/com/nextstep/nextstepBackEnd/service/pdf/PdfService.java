package com.nextstep.nextstepBackEnd.service.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
            InputStream logoStream = getClass().getResourceAsStream("/media/logo03.png");
            if (logoStream == null) {
                throw new FileNotFoundException("El archivo de logo no se encontró en el classpath.");
            }
            Image logo = Image.getInstance(IOUtils.toByteArray(logoStream));
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
            document.add(new Paragraph(" "));
        } catch (Exception e) {
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
