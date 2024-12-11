package com.nextstep.nextstepBackEnd.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.nextstep.nextstepBackEnd.model.SimulacionDTO;
import com.nextstep.nextstepBackEnd.service.pdf.InformePdfService;
import com.nextstep.nextstepBackEnd.service.pdf.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdfServiceTest {

    @InjectMocks
    private PdfService pdfService;

    @Test
    void iniciarDocumento_ShouldReturnValidDocument() throws DocumentException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = pdfService.iniciarDocumento(byteArrayOutputStream);

            assertNotNull(document, "El documento no debería ser nulo.");
            assertTrue(document.isOpen(), "El documento debería estar abierto.");

            // Validar que el logo haya sido agregado al documento
            // No hay forma directa de validar el contenido del PDF aquí,
            // pero al menos confirmamos que el documento se creó y está abierto.
        } catch (IOException e) {
            fail("Error inesperado al intentar cerrar el ByteArrayOutputStream: " + e.getMessage());
        }
    }

    @Test
    void cerrarDocumento_ShouldCloseOpenDocument() {
        Document document = new Document();
        document.open();

        assertTrue(document.isOpen(), "El documento debería estar abierto antes de cerrarlo.");

        pdfService.cerrarDocumento(document);

        assertFalse(document.isOpen(), "El documento debería estar cerrado después de llamar a cerrarDocumento.");
    }

    @Test
    void cerrarDocumento_ShouldDoNothingIfDocumentIsNull() {
        // Este test asegura que no ocurre una excepción si se pasa un documento nulo
        pdfService.cerrarDocumento(null);
    }

    @Test
    void cerrarDocumento_ShouldDoNothingIfDocumentIsAlreadyClosed() {
        Document document = new Document();

        assertFalse(document.isOpen(), "El documento ya debería estar cerrado antes de llamar a cerrarDocumento.");

        pdfService.cerrarDocumento(document);

        assertFalse(document.isOpen(), "El documento debería permanecer cerrado después de llamar a cerrarDocumento.");
    }
}
