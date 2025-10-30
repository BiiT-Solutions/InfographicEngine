package com.biit.infographic.core.pdf;

/*-
 * #%L
 * Infographic Engine v2 (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.server.utils.pdf.PdfDocument;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public abstract class InfographicPdf extends PdfDocument {

    protected InfographicPdf() {
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
