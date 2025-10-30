package com.biit.infographic.core.models;

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

import com.biit.infographic.core.generators.JpegGenerator;
import com.biit.infographic.core.models.svg.utils.SvgFiltering;

import java.util.ArrayList;

public class GeneratedInfographicAsJpegDTO extends GeneratedInfographicAsImageDTO {

    public static GeneratedInfographicAsJpegDTO from(GeneratedInfographicDTO generatedInfographicDTO) {
        if (generatedInfographicDTO == null) {
            return null;
        }
        final GeneratedInfographicAsJpegDTO generatedInfographicAsJpegDTO = new GeneratedInfographicAsJpegDTO();
        generatedInfographicAsJpegDTO.setId(generatedInfographicDTO.getId());
        generatedInfographicAsJpegDTO.setFormName(generatedInfographicDTO.getFormName());
        generatedInfographicAsJpegDTO.setFormVersion(generatedInfographicDTO.getFormVersion());
        generatedInfographicAsJpegDTO.setOrganization(generatedInfographicDTO.getOrganization());
        generatedInfographicAsJpegDTO.setCreatedBy(generatedInfographicDTO.getCreatedBy());
        generatedInfographicAsJpegDTO.setContents(new ArrayList<>());
        for (String svg : generatedInfographicDTO.getSvgContents()) {
            svg = SvgFiltering.cleanUpCode(svg);
            generatedInfographicAsJpegDTO.getContents().add(JpegGenerator.generate(svg));
        }
        return generatedInfographicAsJpegDTO;
    }
}
