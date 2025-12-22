package com.hmvsoluciones.saas.service.mapper;

import static com.hmvsoluciones.saas.domain.NegocioAsserts.*;
import static com.hmvsoluciones.saas.domain.NegocioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NegocioMapperTest {

  private NegocioMapper negocioMapper;

  @BeforeEach
  void setUp() {
    negocioMapper = new NegocioMapperImpl();
  }

  @Test
  void shouldConvertToDtoAndBack() {
    var expected = getNegocioSample1();
    var actual = negocioMapper.toEntity(negocioMapper.toDto(expected));
    assertNegocioAllPropertiesEquals(expected, actual);
  }
}
