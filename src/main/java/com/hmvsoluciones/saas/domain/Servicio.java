package com.hmvsoluciones.saas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A Servicio.
 */
@Entity
@Table(name = "servicio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Servicio extends AbstractAuditingEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_atencion", nullable = false)
    private Instant fechaAtencion;

    @NotNull
    @Column(name = "informe", nullable = false)
    private String informe;

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

    public String getInforme() {
        return this.informe;
    }

    public Servicio informe(String informe) {
        this.setInforme(informe);
        return this;
    }

    public void setInforme(String informe) {
        this.informe = informe;
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
            ", informe='" + getInforme() + "'" +
            ", precio=" + getPrecio() +
            "}";
    }
}
