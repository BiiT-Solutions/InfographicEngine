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

    private List<byte[]> jpegContents;

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

    public List<byte[]> getJpegContents() {
        return jpegContents;
    }

    public void setJpegContents(List<byte[]> jpegContents) {
        this.jpegContents = jpegContents;
    }

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
        generatedInfographicAsJpegDTO.jpegContents = new ArrayList<>();
        for (String svg : generatedInfographicDTO.getSvgContents()) {
            generatedInfographicAsJpegDTO.jpegContents.add(JpegGenerator.generate(svg));
        }
        return generatedInfographicAsJpegDTO;
    }
}
