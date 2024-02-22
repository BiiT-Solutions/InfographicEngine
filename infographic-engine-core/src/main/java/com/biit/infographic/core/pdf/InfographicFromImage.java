package com.biit.infographic.core.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;
import java.util.List;

public class InfographicFromImage extends InfographicPdf {

    private final List<byte[]> pngs;

    public InfographicFromImage(List<byte[]> pngs) {
        this.pngs = pngs;
    }

    @Override
    protected void createContent(Document document, PdfWriter writer) throws DocumentException {
        this.pngs.forEach(png -> {
            try {

                final Image image = Image.getInstance(png);
                document.add(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
