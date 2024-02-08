package com.biit.infographic.core.models;

import com.biit.infographic.core.generators.JpegGenerator;
import com.biit.server.controllers.models.ElementDTO;

import java.util.ArrayList;
import java.util.List;

public class GeneratedInfographicAsJpegDTO extends ElementDTO<Long> {

    private Long id;

    private String formName;

    private int formVersion;

    private Long organizationId;

    private String createdBy;

    private List<byte[]> pngContents;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public int getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(int formVersion) {
        this.formVersion = formVersion;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<byte[]> getPngContents() {
        return pngContents;
    }

    public void setPngContents(List<byte[]> pngContents) {
        this.pngContents = pngContents;
    }

    public static GeneratedInfographicAsJpegDTO from(GeneratedInfographicDTO generatedInfographicDTO) {
        final GeneratedInfographicAsJpegDTO generatedInfographicAsPngDTO = new GeneratedInfographicAsJpegDTO();
        generatedInfographicAsPngDTO.setId(generatedInfographicDTO.getId());
        generatedInfographicAsPngDTO.setFormName(generatedInfographicDTO.getFormName());
        generatedInfographicAsPngDTO.setFormVersion(generatedInfographicDTO.getFormVersion());
        generatedInfographicAsPngDTO.setOrganizationId(generatedInfographicDTO.getOrganizationId());
        generatedInfographicAsPngDTO.setCreatedBy(generatedInfographicDTO.getCreatedBy());
        generatedInfographicAsPngDTO.pngContents = new ArrayList<>();
        for (String svg : generatedInfographicDTO.getSvgContents()) {
            generatedInfographicAsPngDTO.pngContents.add(JpegGenerator.generate(svg));
        }
        return generatedInfographicAsPngDTO;
    }
}
