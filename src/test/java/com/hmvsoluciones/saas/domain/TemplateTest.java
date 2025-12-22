package com.hmvsoluciones.saas.domain;

import static com.hmvsoluciones.saas.domain.TemplateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hmvsoluciones.saas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TemplateTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Template.class);
    Template template1 = getTemplateSample1();
    Template template2 = new Template();
    assertThat(template1).isNotEqualTo(template2);

    template2.setId(template1.getId());
    assertThat(template1).isEqualTo(template2);

    template2 = getTemplateSample2();
    assertThat(template1).isNotEqualTo(template2);
  }
}
