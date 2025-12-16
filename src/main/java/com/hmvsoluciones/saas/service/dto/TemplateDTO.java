package com.hmvsoluciones.saas.service.dto;

import com.hmvsoluciones.saas.domain.AbstractAuditingEntity;
import jakarta.validation.constraints.*;
import java.util.Objects;

/**
 * A DTO for the {@link com.hmvsoluciones.saas.domain.Template} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateDTO extends AbstractAuditingEntity<Long> {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @NotNull
    private String contenidoMarkdown;

    @Size(max = 200)
    private String variables;

    private Boolean activo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContenidoMarkdown() {
        return contenidoMarkdown;
    }

    public void setContenidoMarkdown(String contenidoMarkdown) {
        this.contenidoMarkdown = contenidoMarkdown;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplateDTO)) {
            return false;
        }

        TemplateDTO templateDTO = (TemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, templateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", contenidoMarkdown='" + getContenidoMarkdown() + "'" +
            ", variables='" + getVariables() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
