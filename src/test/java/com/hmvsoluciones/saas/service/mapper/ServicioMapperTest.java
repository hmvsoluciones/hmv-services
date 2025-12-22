package com.hmvsoluciones.saas.service.mapper;

import static com.hmvsoluciones.saas.domain.ServicioAsserts.*;
import static com.hmvsoluciones.saas.domain.ServicioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServicioMapperTest {

  private ServicioMapper servicioMapper;

  @BeforeEach
  void setUp() {
    servicioMapper = new ServicioMapperImpl();
  }

  @Test
  void shouldConvertToDtoAndBack() {
    var expected = getServicioSample1();
    var actual = servicioMapper.toEntity(servicioMapper.toDto(expected));
    assertServicioAllPropertiesEquals(expected, actual);
  }
}
