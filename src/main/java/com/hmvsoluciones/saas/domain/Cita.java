package com.hmvsoluciones.saas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hmvsoluciones.saas.domain.enumeration.EstadoCita;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A Cita.
 */
@Entity
@Table(name = "cita")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Min(value = 0)
    @Max(value = 1000)
    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @NotNull
    @Size(max = 200)
    @Column(name = "descripcion", length = 200, nullable = false)
    private String descripcion;

    @Column(name = "es_urgente")
    private Boolean esUrgente;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cita")
    private EstadoCita estadoCita;

    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    private Empleado empleado;

    @JsonIgnoreProperties(value = { "cita" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cita")
    private Servicio servicio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cita id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Cita fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return this.horaInicio;
    }

    public Cita horaInicio(LocalTime horaInicio) {
        this.setHoraInicio(horaInicio);
        return this;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getDuracionMinutos() {
        return this.duracionMinutos;
    }

    public Cita duracionMinutos(Integer duracionMinutos) {
        this.setDuracionMinutos(duracionMinutos);
        return this;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Cita descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEsUrgente() {
        return this.esUrgente;
    }

    public Cita esUrgente(Boolean esUrgente) {
        this.setEsUrgente(esUrgente);
        return this;
    }

    public void setEsUrgente(Boolean esUrgente) {
        this.esUrgente = esUrgente;
    }

    public EstadoCita getEstadoCita() {
        return this.estadoCita;
    }

    public Cita estadoCita(EstadoCita estadoCita) {
        this.setEstadoCita(estadoCita);
        return this;
    }

    public void setEstadoCita(EstadoCita estadoCita) {
        this.estadoCita = estadoCita;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cita cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Cita empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    public Servicio getServicio() {
        return this.servicio;
    }

    public void setServicio(Servicio servicio) {
        if (this.servicio != null) {
            this.servicio.setCita(null);
        }
        if (servicio != null) {
            servicio.setCita(this);
        }
        this.servicio = servicio;
    }

    public Cita servicio(Servicio servicio) {
        this.setServicio(servicio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cita)) {
            return false;
        }
        return getId() != null && getId().equals(((Cita) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cita{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", duracionMinutos=" + getDuracionMinutos() +
            ", descripcion='" + getDescripcion() + "'" +
            ", esUrgente='" + getEsUrgente() + "'" +
            ", estadoCita='" + getEstadoCita() + "'" +
            "}";
    }
}
