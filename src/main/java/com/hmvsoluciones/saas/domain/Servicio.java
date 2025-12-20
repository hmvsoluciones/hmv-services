package com.hmvsoluciones.saas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A Servicio.
 */
@Entity
@Table(name = "servicio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_atencion", nullable = false)
    private Instant fechaAtencion;

    @Lob
    @Column(name = "contenido", nullable = false)
    private String contenido;

    @Column(name = "precio", precision = 21, scale = 2)
    private BigDecimal precio;

    @JsonIgnoreProperties(value = { "cliente", "empleado", "servicio" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Cita cita;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Servicio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaAtencion() {
        return this.fechaAtencion;
    }

    public Servicio fechaAtencion(Instant fechaAtencion) {
        this.setFechaAtencion(fechaAtencion);
        return this;
    }

    public void setFechaAtencion(Instant fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public String getContenido() {
        return this.contenido;
    }

    public Servicio contenido(String contenido) {
        this.setContenido(contenido);
        return this;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public BigDecimal getPrecio() {
        return this.precio;
    }

    public Servicio precio(BigDecimal precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Cita getCita() {
        return this.cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Servicio cita(Cita cita) {
        this.setCita(cita);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Servicio)) {
            return false;
        }
        return getId() != null && getId().equals(((Servicio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Servicio{" +
            "id=" + getId() +
            ", fechaAtencion='" + getFechaAtencion() + "'" +
            ", contenido='" + getContenido() + "'" +
            ", precio=" + getPrecio() +
            "}";
    }
}
