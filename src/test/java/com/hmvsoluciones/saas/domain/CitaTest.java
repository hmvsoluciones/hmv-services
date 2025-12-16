package com.hmvsoluciones.saas.domain;

import static com.hmvsoluciones.saas.domain.CitaTestSamples.*;
import static com.hmvsoluciones.saas.domain.ClienteTestSamples.*;
import static com.hmvsoluciones.saas.domain.EmpleadoTestSamples.*;
import static com.hmvsoluciones.saas.domain.ServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.saas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CitaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cita.class);
        Cita cita1 = getCitaSample1();
        Cita cita2 = new Cita();
        assertThat(cita1).isNotEqualTo(cita2);

        cita2.setId(cita1.getId());
        assertThat(cita1).isEqualTo(cita2);

        cita2 = getCitaSample2();
        assertThat(cita1).isNotEqualTo(cita2);
    }

    @Test
    void clienteTest() {
        Cita cita = getCitaRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        cita.setCliente(clienteBack);
        assertThat(cita.getCliente()).isEqualTo(clienteBack);

        cita.cliente(null);
        assertThat(cita.getCliente()).isNull();
    }

    @Test
    void empleadoTest() {
        Cita cita = getCitaRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        cita.setEmpleado(empleadoBack);
        assertThat(cita.getEmpleado()).isEqualTo(empleadoBack);

        cita.empleado(null);
        assertThat(cita.getEmpleado()).isNull();
    }

    @Test
    void servicioTest() {
        Cita cita = getCitaRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        cita.setServicio(servicioBack);
        assertThat(cita.getServicio()).isEqualTo(servicioBack);
        assertThat(servicioBack.getCita()).isEqualTo(cita);

        cita.servicio(null);
        assertThat(cita.getServicio()).isNull();
        assertThat(servicioBack.getCita()).isNull();
    }
}
