package com.biit.infographic.persistence.entities;

import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "drools_result",
        indexes = {
                @Index(name = "ind_name", columnList = "form_name"),
                @Index(name = "ind_version", columnList = "form_version"),
                @Index(name = "ind_organization", columnList = "organization_id")
        })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DroolsResult extends Element<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "form_name")
    private String formName;

    @Column(name = "form_version")
    private int formVersion;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "drools_content", columnDefinition = "TEXT", nullable = false)
    private String form;


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

    public void setFormVersion(Integer formVersion) {
        this.formVersion = Objects.requireNonNullElse(formVersion, 1);
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

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return "DroolsResult{"
                + "formName='" + formName + '\''
                + ", formVersion=" + formVersion
                + '}';
    }
}
