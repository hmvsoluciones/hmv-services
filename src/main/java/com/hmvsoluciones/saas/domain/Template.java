package com.hmvsoluciones.saas.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * A Template.
 */
@Entity
@Table(name = "template")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Template extends AbstractAuditingEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "contenido_markdown", nullable = false)
    private String contenidoMarkdown;

    @Size(max = 200)
    @Column(name = "variables", length = 200)
    private String variables;

    @Column(name = "activo")
    private Boolean activo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Template id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Template nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContenidoMarkdown() {
        return this.contenidoMarkdown;
    }

    public Template contenidoMarkdown(String contenidoMarkdown) {
        this.setContenidoMarkdown(contenidoMarkdown);
        return this;
    }

    public void setContenidoMarkdown(String contenidoMarkdown) {
        this.contenidoMarkdown = contenidoMarkdown;
    }

    public String getVariables() {
        return this.variables;
    }

    public Template variables(String variables) {
        this.setVariables(variables);
        return this;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Template activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Template)) {
            return false;
        }
        return getId() != null && getId().equals(((Template) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Template{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", contenidoMarkdown='" + getContenidoMarkdown() + "'" +
            ", variables='" + getVariables() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
