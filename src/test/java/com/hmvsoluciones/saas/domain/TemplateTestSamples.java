package com.hmvsoluciones.saas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Template getTemplateSample1() {
        return new Template().id(1L).nombre("nombre1").contenidoMarkdown("contenidoMarkdown1").variables("variables1");
    }

    public static Template getTemplateSample2() {
        return new Template().id(2L).nombre("nombre2").contenidoMarkdown("contenidoMarkdown2").variables("variables2");
    }

    public static Template getTemplateRandomSampleGenerator() {
        return new Template()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .contenidoMarkdown(UUID.randomUUID().toString())
            .variables(UUID.randomUUID().toString());
    }
}
