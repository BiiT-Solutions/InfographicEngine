package com.biit.infographic.core.pdf;

import com.biit.server.utils.pdf.PdfDocument;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public abstract class InfographicPdf extends PdfDocument {

    private static final int DEFAULT_RIGHT_MARGIN = 30;
    private static final int DEFAULT_LEFT_MARGIN = 30;
    private static final int DEFAULT_TOP_MARGIN = 30;
    private static final int DEFAULT_BOTTOM_MARGIN = 30;

    public InfographicPdf() {
        super();
        setRightMargin(DEFAULT_RIGHT_MARGIN);
        setLeftMargin(DEFAULT_LEFT_MARGIN);
        setTopMargin(DEFAULT_TOP_MARGIN);
        setBottomMargin(DEFAULT_BOTTOM_MARGIN);
    }


    @Override
    protected void addDocumentWriterEvents(PdfWriter writer) {
        // No background.
    }

    @Override
    protected Rectangle getPageSize() {
        return PageSize.A4;
    }
}
