package com.hmvsoluciones.saas.service.criteria;

import com.hmvsoluciones.saas.domain.enumeration.EstadoCita;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hmvsoluciones.saas.domain.Cita} entity. This class is used
 * in {@link com.hmvsoluciones.saas.web.rest.CitaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /citas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CitaCriteria implements Serializable, Criteria {

  /**
   * Class for filtering LocalTime
   */
  public static class LocalTimeFilter extends RangeFilter<LocalTime> {

    public LocalTimeFilter() {}

    public LocalTimeFilter(LocalTimeFilter filter) {
      super(filter);
    }

    @Override
    public LocalTimeFilter copy() {
      return new LocalTimeFilter(this);
    }
  }

  /**
   * Class for filtering EstadoCita
   */
  public static class EstadoCitaFilter extends Filter<EstadoCita> {

    public EstadoCitaFilter() {}

    public EstadoCitaFilter(EstadoCitaFilter filter) {
      super(filter);
    }

    @Override
    public EstadoCitaFilter copy() {
      return new EstadoCitaFilter(this);
    }
  }

  private static final long serialVersionUID = 1L;

  private LongFilter id;

  private LocalDateFilter fecha;

  private LocalTimeFilter horaInicio;

  private IntegerFilter duracionMinutos;

  private StringFilter descripcion;

  private BooleanFilter esUrgente;

  private EstadoCitaFilter estadoCita;

  private LongFilter clienteId;

  private LongFilter empleadoId;

  private LongFilter servicioId;

  private Boolean distinct;

  public CitaCriteria() {}

  public CitaCriteria(CitaCriteria other) {
    this.id = other.optionalId().map(LongFilter::copy).orElse(null);
    this.fecha = other.optionalFecha().map(LocalDateFilter::copy).orElse(null);
    this.horaInicio = other.optionalHoraInicio().map(LocalTimeFilter::copy).orElse(null);
    this.duracionMinutos = other.optionalDuracionMinutos().map(IntegerFilter::copy).orElse(null);
    this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
    this.esUrgente = other.optionalEsUrgente().map(BooleanFilter::copy).orElse(null);
    this.estadoCita = other.optionalEstadoCita().map(EstadoCitaFilter::copy).orElse(null);
    this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
    this.empleadoId = other.optionalEmpleadoId().map(LongFilter::copy).orElse(null);
    this.servicioId = other.optionalServicioId().map(LongFilter::copy).orElse(null);
    this.distinct = other.distinct;
  }

  @Override
  public CitaCriteria copy() {
    return new CitaCriteria(this);
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

  public LocalDateFilter getFecha() {
    return fecha;
  }

  public Optional<LocalDateFilter> optionalFecha() {
    return Optional.ofNullable(fecha);
  }

  public LocalDateFilter fecha() {
    if (fecha == null) {
      setFecha(new LocalDateFilter());
    }
    return fecha;
  }

  public void setFecha(LocalDateFilter fecha) {
    this.fecha = fecha;
  }

  public LocalTimeFilter getHoraInicio() {
    return horaInicio;
  }

  public Optional<LocalTimeFilter> optionalHoraInicio() {
    return Optional.ofNullable(horaInicio);
  }

  public LocalTimeFilter horaInicio() {
    if (horaInicio == null) {
      setHoraInicio(new LocalTimeFilter());
    }
    return horaInicio;
  }

  public void setHoraInicio(LocalTimeFilter horaInicio) {
    this.horaInicio = horaInicio;
  }

  public IntegerFilter getDuracionMinutos() {
    return duracionMinutos;
  }

  public Optional<IntegerFilter> optionalDuracionMinutos() {
    return Optional.ofNullable(duracionMinutos);
  }

  public IntegerFilter duracionMinutos() {
    if (duracionMinutos == null) {
      setDuracionMinutos(new IntegerFilter());
    }
    return duracionMinutos;
  }

  public void setDuracionMinutos(IntegerFilter duracionMinutos) {
    this.duracionMinutos = duracionMinutos;
  }

  public StringFilter getDescripcion() {
    return descripcion;
  }

  public Optional<StringFilter> optionalDescripcion() {
    return Optional.ofNullable(descripcion);
  }

  public StringFilter descripcion() {
    if (descripcion == null) {
      setDescripcion(new StringFilter());
    }
    return descripcion;
  }

  public void setDescripcion(StringFilter descripcion) {
    this.descripcion = descripcion;
  }

  public BooleanFilter getEsUrgente() {
    return esUrgente;
  }

  public Optional<BooleanFilter> optionalEsUrgente() {
    return Optional.ofNullable(esUrgente);
  }

  public BooleanFilter esUrgente() {
    if (esUrgente == null) {
      setEsUrgente(new BooleanFilter());
    }
    return esUrgente;
  }

  public void setEsUrgente(BooleanFilter esUrgente) {
    this.esUrgente = esUrgente;
  }

  public EstadoCitaFilter getEstadoCita() {
    return estadoCita;
  }

  public Optional<EstadoCitaFilter> optionalEstadoCita() {
    return Optional.ofNullable(estadoCita);
  }

  public EstadoCitaFilter estadoCita() {
    if (estadoCita == null) {
      setEstadoCita(new EstadoCitaFilter());
    }
    return estadoCita;
  }

  public void setEstadoCita(EstadoCitaFilter estadoCita) {
    this.estadoCita = estadoCita;
  }

  public LongFilter getClienteId() {
    return clienteId;
  }

  public Optional<LongFilter> optionalClienteId() {
    return Optional.ofNullable(clienteId);
  }

  public LongFilter clienteId() {
    if (clienteId == null) {
      setClienteId(new LongFilter());
    }
    return clienteId;
  }

  public void setClienteId(LongFilter clienteId) {
    this.clienteId = clienteId;
  }

  public LongFilter getEmpleadoId() {
    return empleadoId;
  }

  public Optional<LongFilter> optionalEmpleadoId() {
    return Optional.ofNullable(empleadoId);
  }

  public LongFilter empleadoId() {
    if (empleadoId == null) {
      setEmpleadoId(new LongFilter());
    }
    return empleadoId;
  }

  public void setEmpleadoId(LongFilter empleadoId) {
    this.empleadoId = empleadoId;
  }

  public LongFilter getServicioId() {
    return servicioId;
  }

  public Optional<LongFilter> optionalServicioId() {
    return Optional.ofNullable(servicioId);
  }

  public LongFilter servicioId() {
    if (servicioId == null) {
      setServicioId(new LongFilter());
    }
    return servicioId;
  }

  public void setServicioId(LongFilter servicioId) {
    this.servicioId = servicioId;
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
    final CitaCriteria that = (CitaCriteria) o;
    return (
      Objects.equals(id, that.id) &&
      Objects.equals(fecha, that.fecha) &&
      Objects.equals(horaInicio, that.horaInicio) &&
      Objects.equals(duracionMinutos, that.duracionMinutos) &&
      Objects.equals(descripcion, that.descripcion) &&
      Objects.equals(esUrgente, that.esUrgente) &&
      Objects.equals(estadoCita, that.estadoCita) &&
      Objects.equals(clienteId, that.clienteId) &&
      Objects.equals(empleadoId, that.empleadoId) &&
      Objects.equals(servicioId, that.servicioId) &&
      Objects.equals(distinct, that.distinct)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      id,
      fecha,
      horaInicio,
      duracionMinutos,
      descripcion,
      esUrgente,
      estadoCita,
      clienteId,
      empleadoId,
      servicioId,
      distinct
    );
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "CitaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFecha().map(f -> "fecha=" + f + ", ").orElse("") +
            optionalHoraInicio().map(f -> "horaInicio=" + f + ", ").orElse("") +
            optionalDuracionMinutos().map(f -> "duracionMinutos=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalEsUrgente().map(f -> "esUrgente=" + f + ", ").orElse("") +
            optionalEstadoCita().map(f -> "estadoCita=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalEmpleadoId().map(f -> "empleadoId=" + f + ", ").orElse("") +
            optionalServicioId().map(f -> "servicioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
