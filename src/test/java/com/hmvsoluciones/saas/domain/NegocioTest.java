package com.hmvsoluciones.saas.domain;

import static com.hmvsoluciones.saas.domain.NegocioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.saas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NegocioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Negocio.class);
        Negocio negocio1 = getNegocioSample1();
        Negocio negocio2 = new Negocio();
        assertThat(negocio1).isNotEqualTo(negocio2);

        negocio2.setId(negocio1.getId());
        assertThat(negocio1).isEqualTo(negocio2);

        negocio2 = getNegocioSample2();
        assertThat(negocio1).isNotEqualTo(negocio2);
    }
}
