package com.hmvsoluciones.saas.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Negocio.
 */
@Entity
@Table(name = "negocio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Negocio implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @NotNull
  @Size(max = 150)
  @Column(name = "nombre", length = 150, nullable = false)
  private String nombre;

  @NotNull
  @Size(max = 150)
  @Column(name = "responsable", length = 150, nullable = false)
  private String responsable;

  @Size(max = 30)
  @Column(name = "celular", length = 30)
  private String celular;

  @Size(max = 256)
  @Column(name = "correo", length = 256)
  private String correo;

  @NotNull
  @Column(name = "subscription_key", nullable = false)
  private String subscriptionKey;

  @Column(name = "es_activo")
  private Boolean esActivo;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public Negocio id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return this.nombre;
  }

  public Negocio nombre(String nombre) {
    this.setNombre(nombre);
    return this;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getResponsable() {
    return this.responsable;
  }

  public Negocio responsable(String responsable) {
    this.setResponsable(responsable);
    return this;
  }

  public void setResponsable(String responsable) {
    this.responsable = responsable;
  }

  public String getCelular() {
    return this.celular;
  }

  public Negocio celular(String celular) {
    this.setCelular(celular);
    return this;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public String getCorreo() {
    return this.correo;
  }

  public Negocio correo(String correo) {
    this.setCorreo(correo);
    return this;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getSubscriptionKey() {
    return this.subscriptionKey;
  }

  public Negocio subscriptionKey(String subscriptionKey) {
    this.setSubscriptionKey(subscriptionKey);
    return this;
  }

  public void setSubscriptionKey(String subscriptionKey) {
    this.subscriptionKey = subscriptionKey;
  }

  public Boolean getEsActivo() {
    return this.esActivo;
  }

  public Negocio esActivo(Boolean esActivo) {
    this.setEsActivo(esActivo);
    return this;
  }

  public void setEsActivo(Boolean esActivo) {
    this.esActivo = esActivo;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Negocio)) {
      return false;
    }
    return getId() != null && getId().equals(((Negocio) o).getId());
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "Negocio{" +
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
