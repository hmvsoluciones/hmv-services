package com.hmvsoluciones.saas.service.dto;

import com.hmvsoluciones.saas.domain.enumeration.EstadoCita;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.hmvsoluciones.saas.domain.Cita} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CitaDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime horaInicio;

    @Min(value = 0)
    @Max(value = 1000)
    private Integer duracionMinutos;

    @NotNull
    @Size(max = 200)
    private String descripcion;

    private Boolean esUrgente;

    private EstadoCita estadoCita;

    @NotNull
    private ClienteDTO cliente;

    private EmpleadoDTO empleado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEsUrgente() {
        return esUrgente;
    }

    public void setEsUrgente(Boolean esUrgente) {
        this.esUrgente = esUrgente;
    }

    public EstadoCita getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(EstadoCita estadoCita) {
        this.estadoCita = estadoCita;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CitaDTO)) {
            return false;
        }

        CitaDTO citaDTO = (CitaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, citaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitaDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", duracionMinutos=" + getDuracionMinutos() +
            ", descripcion='" + getDescripcion() + "'" +
            ", esUrgente='" + getEsUrgente() + "'" +
            ", estadoCita='" + getEstadoCita() + "'" +
            ", cliente=" + getCliente() +
            ", empleado=" + getEmpleado() +
            "}";
    }
}
