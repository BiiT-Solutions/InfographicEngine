package com.biit.infographic.persistence.entities;

import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "generated_infographic",
        indexes = {
                @Index(name = "ind_name", columnList = "form_name"),
                @Index(name = "ind_version", columnList = "form_version"),
                @Index(name = "ind_organization", columnList = "organization_id")
        })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class GeneratedInfographic extends Element<Long> {

    @Column(name = "form_name")
    private String formName;

    @Column(name = "form_version")
    private int formVersion;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "created_by")
    private String createdBy;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "svg_contents", joinColumns = @JoinColumn(name = "generated_infographic_id"))
    @Column(name = "svg_content", columnDefinition = "TEXT", nullable = false)
    private List<String> svgContents;

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getSvgContents() {
        return svgContents;
    }

    public void setSvgContents(List<String> svgContents) {
        this.svgContents = svgContents;
    }

    @Override
    public String toString() {
        return "GeneratedInfographic{"
                + "formName='" + formName + '\''
                + ", formVersion=" + formVersion
                + '}';
    }
}
