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

import com.biit.infographic.core.generators.PngGenerator;
import com.biit.infographic.core.models.svg.utils.SvgFiltering;

import java.util.ArrayList;

public class GeneratedInfographicAsPngDTO extends GeneratedInfographicAsImageDTO {

    public static GeneratedInfographicAsPngDTO from(GeneratedInfographicDTO generatedInfographicDTO) {
        if (generatedInfographicDTO == null) {
            return null;
        }

        final GeneratedInfographicAsPngDTO generatedInfographicAsPngDTO = new GeneratedInfographicAsPngDTO();
        generatedInfographicAsPngDTO.setId(generatedInfographicDTO.getId());
        generatedInfographicAsPngDTO.setFormName(generatedInfographicDTO.getFormName());
        generatedInfographicAsPngDTO.setFormVersion(generatedInfographicDTO.getFormVersion());
        generatedInfographicAsPngDTO.setOrganization(generatedInfographicDTO.getOrganization());
        generatedInfographicAsPngDTO.setCreatedBy(generatedInfographicDTO.getCreatedBy());
        generatedInfographicAsPngDTO.setContents(new ArrayList<>());
        for (String svg : generatedInfographicDTO.getSvgContents()) {
            svg = SvgFiltering.cleanUpCode(svg);
            generatedInfographicAsPngDTO.getContents().add(PngGenerator.generate(svg));
        }
        return generatedInfographicAsPngDTO;
    }
}
