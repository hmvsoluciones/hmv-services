package com.hmvsoluciones.saas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hmvsoluciones.saas.domain.Negocio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NegocioDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String nombre;

    @NotNull
    @Size(max = 150)
    private String responsable;

    @Size(max = 30)
    private String celular;

    @Size(max = 256)
    private String correo;

    @NotNull
    private String subscriptionKey;

    private Boolean esActivo;

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

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getSubscriptionKey() {
        return subscriptionKey;
    }

    public void setSubscriptionKey(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NegocioDTO)) {
            return false;
        }

        NegocioDTO negocioDTO = (NegocioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, negocioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NegocioDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", celular='" + getCelular() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", subscriptionKey='" + getSubscriptionKey() + "'" +
            ", esActivo='" + getEsActivo() + "'" +
            "}";
    }
}
