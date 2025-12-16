package com.hmvsoluciones.saas.domain;

import static com.hmvsoluciones.saas.domain.CitaTestSamples.*;
import static com.hmvsoluciones.saas.domain.ServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.saas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Servicio.class);
        Servicio servicio1 = getServicioSample1();
        Servicio servicio2 = new Servicio();
        assertThat(servicio1).isNotEqualTo(servicio2);

        servicio2.setId(servicio1.getId());
        assertThat(servicio1).isEqualTo(servicio2);

        servicio2 = getServicioSample2();
        assertThat(servicio1).isNotEqualTo(servicio2);
    }

    @Test
    void citaTest() {
        Servicio servicio = getServicioRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        servicio.setCita(citaBack);
        assertThat(servicio.getCita()).isEqualTo(citaBack);

        servicio.cita(null);
        assertThat(servicio.getCita()).isNull();
    }
}
