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

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;
import java.util.List;

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
                image.setAlignment(Element.ALIGN_CENTER);
                document.add(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
