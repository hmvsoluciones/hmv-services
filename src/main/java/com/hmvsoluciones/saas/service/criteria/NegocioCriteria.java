package com.hmvsoluciones.saas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hmvsoluciones.saas.domain.Negocio} entity. This class is used
 * in {@link com.hmvsoluciones.saas.web.rest.NegocioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /negocios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NegocioCriteria implements Serializable, Criteria {

  private static final long serialVersionUID = 1L;

  private LongFilter id;

  private StringFilter nombre;

  private StringFilter responsable;

  private StringFilter celular;

  private StringFilter correo;

  private StringFilter subscriptionKey;

  private BooleanFilter esActivo;

  private Boolean distinct;

  public NegocioCriteria() {}

  public NegocioCriteria(NegocioCriteria other) {
    this.id = other.optionalId().map(LongFilter::copy).orElse(null);
    this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
    this.responsable = other.optionalResponsable().map(StringFilter::copy).orElse(null);
    this.celular = other.optionalCelular().map(StringFilter::copy).orElse(null);
    this.correo = other.optionalCorreo().map(StringFilter::copy).orElse(null);
    this.subscriptionKey = other.optionalSubscriptionKey().map(StringFilter::copy).orElse(null);
    this.esActivo = other.optionalEsActivo().map(BooleanFilter::copy).orElse(null);
    this.distinct = other.distinct;
  }

  @Override
  public NegocioCriteria copy() {
    return new NegocioCriteria(this);
  }

  public LongFilter getId() {
    return id;
  }

  public Optional<LongFilter> optionalId() {
    return Optional.ofNullable(id);
  }

  public LongFilter id() {
    if (id == null) {
      setId(new LongFilter());
    }
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public StringFilter getNombre() {
    return nombre;
  }

  public Optional<StringFilter> optionalNombre() {
    return Optional.ofNullable(nombre);
  }

  public StringFilter nombre() {
    if (nombre == null) {
      setNombre(new StringFilter());
    }
    return nombre;
  }

  public void setNombre(StringFilter nombre) {
    this.nombre = nombre;
  }

  public StringFilter getResponsable() {
    return responsable;
  }

  public Optional<StringFilter> optionalResponsable() {
    return Optional.ofNullable(responsable);
  }

  public StringFilter responsable() {
    if (responsable == null) {
      setResponsable(new StringFilter());
    }
    return responsable;
  }

  public void setResponsable(StringFilter responsable) {
    this.responsable = responsable;
  }

  public StringFilter getCelular() {
    return celular;
  }

  public Optional<StringFilter> optionalCelular() {
    return Optional.ofNullable(celular);
  }

  public StringFilter celular() {
    if (celular == null) {
      setCelular(new StringFilter());
    }
    return celular;
  }

  public void setCelular(StringFilter celular) {
    this.celular = celular;
  }

  public StringFilter getCorreo() {
    return correo;
  }

  public Optional<StringFilter> optionalCorreo() {
    return Optional.ofNullable(correo);
  }

  public StringFilter correo() {
    if (correo == null) {
      setCorreo(new StringFilter());
    }
    return correo;
  }

  public void setCorreo(StringFilter correo) {
    this.correo = correo;
  }

  public StringFilter getSubscriptionKey() {
    return subscriptionKey;
  }

  public Optional<StringFilter> optionalSubscriptionKey() {
    return Optional.ofNullable(subscriptionKey);
  }

  public StringFilter subscriptionKey() {
    if (subscriptionKey == null) {
      setSubscriptionKey(new StringFilter());
    }
    return subscriptionKey;
  }

  public void setSubscriptionKey(StringFilter subscriptionKey) {
    this.subscriptionKey = subscriptionKey;
  }

  public BooleanFilter getEsActivo() {
    return esActivo;
  }

  public Optional<BooleanFilter> optionalEsActivo() {
    return Optional.ofNullable(esActivo);
  }

  public BooleanFilter esActivo() {
    if (esActivo == null) {
      setEsActivo(new BooleanFilter());
    }
    return esActivo;
  }

  public void setEsActivo(BooleanFilter esActivo) {
    this.esActivo = esActivo;
  }

  public Boolean getDistinct() {
    return distinct;
  }

  public Optional<Boolean> optionalDistinct() {
    return Optional.ofNullable(distinct);
  }

  public Boolean distinct() {
    if (distinct == null) {
      setDistinct(true);
    }
    return distinct;
  }

  public void setDistinct(Boolean distinct) {
    this.distinct = distinct;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final NegocioCriteria that = (NegocioCriteria) o;
    return (
      Objects.equals(id, that.id) &&
      Objects.equals(nombre, that.nombre) &&
      Objects.equals(responsable, that.responsable) &&
      Objects.equals(celular, that.celular) &&
      Objects.equals(correo, that.correo) &&
      Objects.equals(subscriptionKey, that.subscriptionKey) &&
      Objects.equals(esActivo, that.esActivo) &&
      Objects.equals(distinct, that.distinct)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nombre, responsable, celular, correo, subscriptionKey, esActivo, distinct);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "NegocioCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalResponsable().map(f -> "responsable=" + f + ", ").orElse("") +
            optionalCelular().map(f -> "celular=" + f + ", ").orElse("") +
            optionalCorreo().map(f -> "correo=" + f + ", ").orElse("") +
            optionalSubscriptionKey().map(f -> "subscriptionKey=" + f + ", ").orElse("") +
            optionalEsActivo().map(f -> "esActivo=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
