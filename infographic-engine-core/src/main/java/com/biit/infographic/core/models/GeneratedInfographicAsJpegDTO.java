package com.biit.infographic.core.models;

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
        generatedInfographicAsJpegDTO.setOrganizationId(generatedInfographicDTO.getOrganizationId());
        generatedInfographicAsJpegDTO.setCreatedBy(generatedInfographicDTO.getCreatedBy());
        generatedInfographicAsJpegDTO.setContents(new ArrayList<>());
        for (String svg : generatedInfographicDTO.getSvgContents()) {
            svg = SvgFiltering.filterEmbeddedFonts(svg);
            generatedInfographicAsJpegDTO.getContents().add(JpegGenerator.generate(svg));
        }
        return generatedInfographicAsJpegDTO;
    }
}
