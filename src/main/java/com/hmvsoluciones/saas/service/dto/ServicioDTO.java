package com.hmvsoluciones.saas.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.hmvsoluciones.saas.domain.Servicio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioDTO implements Serializable {

  private Long id;

  @NotNull
  private Instant fechaAtencion;

  @Lob
  private String contenido;

  private BigDecimal precio;

  @NotNull
  private CitaDTO cita;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getFechaAtencion() {
    return fechaAtencion;
  }

  public void setFechaAtencion(Instant fechaAtencion) {
    this.fechaAtencion = fechaAtencion;
  }

  public String getContenido() {
    return contenido;
  }

  public void setContenido(String contenido) {
    this.contenido = contenido;
  }

  public BigDecimal getPrecio() {
    return precio;
  }

  public void setPrecio(BigDecimal precio) {
    this.precio = precio;
  }

  public CitaDTO getCita() {
    return cita;
  }

  public void setCita(CitaDTO cita) {
    this.cita = cita;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ServicioDTO)) {
      return false;
    }

    ServicioDTO servicioDTO = (ServicioDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, servicioDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "ServicioDTO{" +
            "id=" + getId() +
            ", fechaAtencion='" + getFechaAtencion() + "'" +
            ", contenido='" + getContenido() + "'" +
            ", precio=" + getPrecio() +
            ", cita=" + getCita() +
            "}";
    }
}
