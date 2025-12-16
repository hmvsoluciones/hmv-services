package com.hmvsoluciones.saas.service.mapper;

import static com.hmvsoluciones.saas.domain.TemplateAsserts.*;
import static com.hmvsoluciones.saas.domain.TemplateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TemplateMapperTest {

    private TemplateMapper templateMapper;

    @BeforeEach
    void setUp() {
        templateMapper = new TemplateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTemplateSample1();
        var actual = templateMapper.toEntity(templateMapper.toDto(expected));
        assertTemplateAllPropertiesEquals(expected, actual);
    }
}
