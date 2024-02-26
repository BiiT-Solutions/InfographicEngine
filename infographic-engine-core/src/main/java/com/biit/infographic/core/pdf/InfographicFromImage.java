package com.biit.infographic.core.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class InfographicFromImage extends InfographicPdf {

    private final List<byte[]> pngs;

    public InfographicFromImage(List<byte[]> pngs) {
        super();
        this.pngs = pngs;
    }

    @Override
    protected void createContent(Document document, PdfWriter writer) throws DocumentException {
        this.pngs.forEach(png -> {
            try {

                final Image image = Image.getInstance(png);
                if (image.getHeight() > getPageSize().getHeight() || image.getWidth() > getPageSize().getWidth()) {
                    image.scaleToFit(getPageSize().getWidth(), getPageSize().getHeight());
                }
                document.add(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
