package com.hmvsoluciones.saas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.saas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NegocioDTOTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(NegocioDTO.class);
    NegocioDTO negocioDTO1 = new NegocioDTO();
    negocioDTO1.setId(1L);
    NegocioDTO negocioDTO2 = new NegocioDTO();
    assertThat(negocioDTO1).isNotEqualTo(negocioDTO2);
    negocioDTO2.setId(negocioDTO1.getId());
    assertThat(negocioDTO1).isEqualTo(negocioDTO2);
    negocioDTO2.setId(2L);
    assertThat(negocioDTO1).isNotEqualTo(negocioDTO2);
    negocioDTO1.setId(null);
    assertThat(negocioDTO1).isNotEqualTo(negocioDTO2);
  }
}
