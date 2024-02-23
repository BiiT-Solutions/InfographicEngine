package com.biit.infographic.core.models;

import com.biit.infographic.core.generators.PngGenerator;
import com.biit.infographic.core.models.svg.utils.SvgFiltering;

import java.util.ArrayList;

public class GeneratedInfographicAsPngDTO extends GeneratedInfographicAsImageDTO {

    public static GeneratedInfographicAsPngDTO from(GeneratedInfographicDTO generatedInfographicDTO) {
        return from(generatedInfographicDTO, true);
    }

    public static GeneratedInfographicAsPngDTO from(GeneratedInfographicDTO generatedInfographicDTO, boolean embedFonts) {
        if (generatedInfographicDTO == null) {
            return null;
        }
        final GeneratedInfographicAsPngDTO generatedInfographicAsPngDTO = new GeneratedInfographicAsPngDTO();
        generatedInfographicAsPngDTO.setId(generatedInfographicDTO.getId());
        generatedInfographicAsPngDTO.setFormName(generatedInfographicDTO.getFormName());
        generatedInfographicAsPngDTO.setFormVersion(generatedInfographicDTO.getFormVersion());
        generatedInfographicAsPngDTO.setOrganizationId(generatedInfographicDTO.getOrganizationId());
        generatedInfographicAsPngDTO.setCreatedBy(generatedInfographicDTO.getCreatedBy());
        generatedInfographicAsPngDTO.setContents(new ArrayList<>());
        for (String svg : generatedInfographicDTO.getSvgContents()) {
            if (!embedFonts) {
                svg = SvgFiltering.filterEmbeddedFonts(svg);
            }
            generatedInfographicAsPngDTO.getContents().add(PngGenerator.generate(svg));
        }
        return generatedInfographicAsPngDTO;
    }
}
