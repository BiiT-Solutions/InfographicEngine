package com.biit.infographic.persistence.entities;

import com.biit.database.encryption.StringCryptoConverter;
import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
                @Index(name = "ind_organization", columnList = "organization")
        })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class GeneratedInfographic extends Element<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "form_name")
    private String formName;

    @Column(name = "form_version")
    private int formVersion;

    @Column(name = "organization")
    private String organization;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "svg_contents", joinColumns = @JoinColumn(name = "generated_infographic_id"))
    @Column(name = "svg_content", columnDefinition = "TEXT", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private List<String> svgContents;

    @Column(name = "drools_submitted_form", columnDefinition = "TEXT")
    @Convert(converter = StringCryptoConverter.class)
    private String droolsSubmittedForm;

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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public List<String> getSvgContents() {
        return svgContents;
    }

    public void setSvgContents(List<String> svgContents) {
        this.svgContents = svgContents;
    }

    public String getDroolsSubmittedForm() {
        return droolsSubmittedForm;
    }

    public void setDroolsSubmittedForm(String droolsSubmittedForm) {
        this.droolsSubmittedForm = droolsSubmittedForm;
    }

    @Override
    public String toString() {
        return "GeneratedInfographic{"
                + "formName='" + formName + '\''
                + ", formVersion=" + formVersion
                + '}';
    }
}
