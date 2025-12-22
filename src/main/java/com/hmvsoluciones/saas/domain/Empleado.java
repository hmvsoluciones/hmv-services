package com.hmvsoluciones.saas.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Empleado.
 */
@Entity
@Table(name = "empleado")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Empleado implements Serializable {

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

  @Size(max = 30)
  @Column(name = "celular", length = 30)
  private String celular;

  @Size(max = 100)
  @Column(name = "especialidad", length = 100)
  private String especialidad;

  @Column(name = "activo")
  private Boolean activo;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public Empleado id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return this.nombre;
  }

  public Empleado nombre(String nombre) {
    this.setNombre(nombre);
    return this;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getCelular() {
    return this.celular;
  }

  public Empleado celular(String celular) {
    this.setCelular(celular);
    return this;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public String getEspecialidad() {
    return this.especialidad;
  }

  public Empleado especialidad(String especialidad) {
    this.setEspecialidad(especialidad);
    return this;
  }

  public void setEspecialidad(String especialidad) {
    this.especialidad = especialidad;
  }

  public Boolean getActivo() {
    return this.activo;
  }

  public Empleado activo(Boolean activo) {
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
    if (!(o instanceof Empleado)) {
      return false;
    }
    return getId() != null && getId().equals(((Empleado) o).getId());
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "Empleado{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", celular='" + getCelular() + "'" +
            ", especialidad='" + getEspecialidad() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
